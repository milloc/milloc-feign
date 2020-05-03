package com.milloc.referfeign.springbootautoconfigure.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示url上的一个query字段
 *
 * @author milloc
 * @date 2020-05-03
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
