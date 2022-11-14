package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

/**
 * 这个注解用于批量设置ContentType，减少重复代码
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentType {
    String[] contentType() default {"application/json;charset=utf-8"};
}
