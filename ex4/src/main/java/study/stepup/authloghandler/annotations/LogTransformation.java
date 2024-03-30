package study.stepup.authloghandler.annotations;

import java.lang.annotation.*;


@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransformation {
    String value() default  "logTransformation";
}