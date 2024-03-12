package study.stepup;

import java.lang.reflect.Proxy;

public class Utils {
    public static <T> T cache(T inObject) {
        try {
            ClassLoader classLoader = inObject.getClass().getClassLoader();
            Class<?>[] interfaces = inObject.getClass().getInterfaces();
            return (T)Proxy.newProxyInstance(classLoader, interfaces, new ObjectInvocationHandler(inObject));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            return  null;
        }
    }
}
