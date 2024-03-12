package study.stepup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ObjectInvocationHandler implements InvocationHandler {
    private final Object object1;
    private final Lock chachLock = new ReentrantLock();
    Thread clearCacheThread;
    Map<String, CacheEntry> caches = new ConcurrentHashMap<>();

    public ObjectInvocationHandler(Object object) throws IllegalAccessException {
        this.object1 = object;
        clearCacheThread = new Thread(() -> {
            while (!clearCacheThread.isInterrupted())
            try {
                for (String key : caches.keySet()) {
                    CacheEntry cacheEntry = caches.get(key);
                    //Очистка устарелых данных. Если время 0, то по ТЗ кэш вечный
                    if (cacheEntry.getExpireTime() != 0 && cacheEntry.getExpireTime() < System.currentTimeMillis()) {
                        caches.remove(key);
                    }
                }
                //wait();
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
        clearCache();

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
            long expireTime = cacheVal > 0 ? cacheVal + System.currentTimeMillis() : 0;
            chachLock.lock();
            if (caches.containsKey(cacheKey)) {
                CacheEntry entry = caches.get(cacheKey);
                entry.setExpireTime(expireTime);
                caches.put(cacheKey, entry);
                chachLock.unlock();
                return entry.getObject();
            } else {
                Object rez = method.invoke(object1, args);
                caches.put(cacheKey, new CacheEntry(expireTime, rez));
                chachLock.unlock();
                return rez;
            }
        }
        return method.invoke(object1, args);
    }


    private void clearCache() {
//    clearCacheThread.getState()
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
