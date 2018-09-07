package com.milog.test.mygradleplugintest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by miloway on 2018/8/16.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FunctionManager {
    String value() default "";
}
