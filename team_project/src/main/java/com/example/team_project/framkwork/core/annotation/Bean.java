package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

/**
 * 普通容器的依赖管理，当某个类带上这个注解，并且有扫描这个包的时候，便会对这个类进行加载
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    /**
     * 依赖的名字，推荐与类名一直
     */
    String value();

    /**
     * 加载的顺序,推荐把顺序和对应的类写到文件去读取，如果能做到的话
     */
    int order() default 0;
}
