package com.example.team_project.framkwork.core.exception;

public class LoadConfigException extends RuntimeException{

    public LoadConfigException(){
        super("【初始化异常】");
    }

    public LoadConfigException(String msg){
        super("【初始化异常】"+" : " + msg);
    }
}
