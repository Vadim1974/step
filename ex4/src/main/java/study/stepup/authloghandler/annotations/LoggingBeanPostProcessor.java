package study.stepup.authloghandler.annotations;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class> loggingBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (Arrays.stream(beanClass.getMethods()).anyMatch(method -> method.isAnnotationPresent(LogTransformation.class))) {
            loggingBeans.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class originalBean = loggingBeans.get(beanName);
        if (originalBean != null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(originalBean);
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                Optional<Method> originalMethod = Arrays.stream(originalBean.getMethods())
                        .filter(method::equals)
                        .findFirst();

                if (originalMethod.isPresent()) {
                    LogTransformation annotation = originalMethod.get().getAnnotation(LogTransformation.class);
                    if (annotation != null) {
//                        Marker marker = MarkerFactory.getMarker(annotation.value());
//                        log.info(marker, "1111");
//                        xxx(annotation.value(), "11112343");
//                        log.info("11111");
                        String beforeArgs = Arrays.asList(args).toString();
                        LocalDateTime beforeDateTime = LocalDateTime.now();
                        Object invoke = proxy.invoke(bean, args);
                        log.info("StartAt:{} {} Before:{} After:{}", beforeDateTime, originalBean.getSimpleName(), beforeArgs, Arrays.asList(args));
                        return invoke;
                    }
                }
                return method.invoke(bean, args);
            });
            return enhancer.create();
        }
        return bean;
    }

    private void getLogger(String logFileName, String msg){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) loggerContext.getLogger("nulldate");
        FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>)logbackLogger.getAppender("NULLDATE-FILE");
        fileAppender.setFile(logFileName);
        logbackLogger.info(msg);
    }
}