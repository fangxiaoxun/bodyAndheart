package com.example.team_project.framkwork.jdbc.annotation;

import java.lang.annotation.*;

/**
 * 使得类的属性能简单的与表中字段对应
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    /**
     * <p>使成员变量名对应数据库的字段名</p>
     */
    String value();

    boolean ignore() default false;
}
