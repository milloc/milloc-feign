package com.milloc.dbfeignspringbootautoconfigure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DBClient
 * 使用方式
 * 在接口上添加该注解 配合使用以下注解
 * {@link org.springframework.web.bind.annotation.RequestMapping RequestMapping}
 * {@link org.springframework.web.bind.annotation.PathVariable PathVariable}
 * {@link org.springframework.web.bind.annotation.RequestParam RequestParam}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DBClient {
}
