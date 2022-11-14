package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {
    /**
     * 若value为默认，则是普通容器加载，若为mvc则是Controller容器
     * @return
     */
    String value() default "";
}
