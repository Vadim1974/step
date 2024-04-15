package study.stepup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//1. Время и способ запуска очистки
//2. Не терять значения
//3. Максимальная параллельность

public class ObjectInvocationHandler implements InvocationHandler {
    private final Integer cacheSize = 100;
    private final Integer sleepMilliSec = 500;
    private final Object object1;
    Thread clearCacheThread;
    Map<String, CacheEntry> caches = new ConcurrentHashMap<>();

    public ObjectInvocationHandler(Object object) {
        this.object1 = object;
        clearCacheThread = new Thread(() -> {
            while (!clearCacheThread.isInterrupted()) {
                try {
                    if (caches.size() > cacheSize) {
                        for (String key : caches.keySet()) {
                            CacheEntry cacheEntry = caches.get(key);
                            if (cacheEntry != null && cacheEntry.isExpired()) {
                                caches.remove(key);
                            }
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(sleepMilliSec);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        clearCacheThread.start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //TODO Очищать кэш не сразу, а в отдельном потоке
        // если в аннотации срока нет, то считаем, что храним бесконечно
        // прописывать время жизни на каждый объект
        // Гарантировать отсутсвие многопоточных проблем
//        clearCache();

        String mementoKey = getMementoKey(object1);
        String invokeMethodName = method.getName();
        Class<?>[] invokeMethodParameters = method.getParameterTypes();
        String cacheKey = mementoKey + invokeMethodName + Arrays.toString(invokeMethodParameters);
        System.out.println("Вызов_" + cacheKey + Arrays.toString(args));

        Method objMethod = object1.getClass().getDeclaredMethod(invokeMethodName, invokeMethodParameters);
        if (objMethod.isAnnotationPresent(Mutator.class)) {
            //TODO создаем мементо, и переключаем текущий кэш
            //не потребовалось, т.к нет вложенной мапы
        }

        if (!mementoKey.isEmpty() && objMethod.isAnnotationPresent(Cache.class)) {
            long cacheVal = objMethod.getAnnotation(Cache.class).value();
            CacheEntry entry = caches.get(cacheKey);
            if (entry != null && !entry.isExpired()) {
                Object rez = entry.getObject();
                caches.put(cacheKey, new CacheEntry(cacheVal, rez));
                return entry.getObject();
            } else {
                Object rez = method.invoke(object1, args);
                caches.put(cacheKey, new CacheEntry(cacheVal, rez));
                return rez;
            }
        }
        return method.invoke(object1, args);
    }


    private String getMementoKey(Object object) {
        try {
            StringBuilder sb = new StringBuilder();
            if (object != null) {
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    sb.append(field.getName()).append(field.get(object).toString());
                }
            }
            return sb.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }
}
