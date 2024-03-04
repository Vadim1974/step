package study.stepup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectInvocationHandler implements InvocationHandler {
    private final Object object1;
    Map<String,Object> caches = new HashMap<>();

    public ObjectInvocationHandler(Object object) {
        this.object1 = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String invokeMethodName = method.getName();
        Class<?>[] invokeMethodParameters = method.getParameterTypes();
        String cacheKey = invokeMethodName + Arrays.toString(invokeMethodParameters);
//        System.out.println("Вызов " + cacheKey);


        Method objMethod = object1.getClass().getDeclaredMethod(invokeMethodName, invokeMethodParameters);
        if (objMethod.isAnnotationPresent(Mutator.class)) {
            caches.clear();
//            System.out.println("cache clearing");
        }

        if (objMethod.isAnnotationPresent(Cache.class)) {
            if (caches.containsKey(cacheKey)) {
//                System.out.println("Get from cache");
                return caches.get(cacheKey);
            }
            else {
                Object rez = method.invoke(object1, args);
                caches.put(cacheKey, rez);
//                System.out.println("Put to cache");
                return rez;
            }
        }

        return method.invoke(object1, args);
    }
}
