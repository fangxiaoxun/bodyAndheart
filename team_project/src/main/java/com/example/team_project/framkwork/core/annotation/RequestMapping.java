package com.example.team_project.framkwork.core.annotation;

import com.example.team_project.framkwork.comment.emun.RequestType;

import java.lang.annotation.*;

/**
 * controller的请求信息
 * 允许在方法和类上写该注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    /**
     * 相当于urlPattern
     * @return
     */
    String value() default "";

    /**
     * Controller的请求地址
     * @return
     */
    String[] urlPattern() default {};

    /**
     * 请求方式
     * @return
     */
    RequestType[] type() default {RequestType.GET,RequestType.POST,RequestType.PUT,RequestType.DELETE};

    /**
     * Controller的优先级，暂时没有作用
     * @return
     */
    int priority() default Integer.MAX_VALUE;
}
