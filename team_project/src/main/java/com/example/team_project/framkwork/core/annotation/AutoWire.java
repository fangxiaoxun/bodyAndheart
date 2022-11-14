package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

/**
 * 引用类型的依赖注入
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWire {
    /**
     * 注入的被容器接管的对象名
     */
    String value() default "";
}
