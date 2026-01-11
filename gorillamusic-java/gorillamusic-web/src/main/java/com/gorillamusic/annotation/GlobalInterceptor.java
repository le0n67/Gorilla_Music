package com.gorillamusic.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Date：2026/1/9  23:48
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface GlobalInterceptor {
    boolean checkLogin() default false;
}
