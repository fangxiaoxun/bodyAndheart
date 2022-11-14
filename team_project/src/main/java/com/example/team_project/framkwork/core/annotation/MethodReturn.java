package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodReturn {
    /**
     * 推荐名字和方法类型的名字一致
     */
    String value();

    int methodOrder();
}
