package com.example.team_project.framkwork.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {
    /**
     * 必须为包路径
     * @return 包路径
     */
    String packetPath();
}
