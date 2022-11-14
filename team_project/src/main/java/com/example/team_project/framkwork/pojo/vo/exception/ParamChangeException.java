package com.example.team_project.framkwork.pojo.vo.exception;

/**
 * ApiMsg类缓存被修改抛出的异常
 */
public class ParamChangeException extends RuntimeException{

    public ParamChangeException() {
    }

    public ParamChangeException(String message) {
        super(message);
    }
}
