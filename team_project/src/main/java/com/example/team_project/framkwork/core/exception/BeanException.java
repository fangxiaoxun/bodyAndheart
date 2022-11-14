package com.example.team_project.framkwork.core.exception;

public class BeanException extends RuntimeException{

    public BeanException() {
        super("【构造Bean异常】");
    }

    public BeanException(String message) {
        super("【构造Bean异常】" + message);
    }
}
