package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

/**
 * 当Controller类带上这个注解，并且包扫描覆盖到这个类的时候，则会进行Controller依赖管理
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value();
}
