package com.example.team_project.framkwork.pojo.vo;

import com.example.team_project.framkwork.comment.emun.ConditionType;
import com.example.team_project.framkwork.pojo.vo.exception.ParamChangeException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Map;

/**
 * 用于向前端传递格式统一的json数据
 */
public class ApiMsg implements Serializable {
    /** 状态码，用于给前端判断响应情况 */
    private int code;
    /** 响应情况说明 */
    private String message;
    /** 响应数据，可能为空 */
    private Map<String,Object> data;
    /**
     * 常用的对象，这样写，只要不改变内部数据，则只需要实例化一次
     */
    @JsonIgnore
    private static final ApiMsg ERROR = new ApiMsg(200,"error");
    /**
     * 常用的对象，这样写，只要不改变内部数据，则只需要实例化一次
     */
    @JsonIgnore
    private static final ApiMsg NOT_LOGIN = new ApiMsg(200,"notLogin");


    private ApiMsg(ConditionType type){
        this.code = type.getCode();
        this.message = type.getMsg();
    }

    public ApiMsg(int code,String msg){
        this.code = code;
        this.message = msg;
    }

    /**
     * warn:不可对返回值进行更改
     */
    public static ApiMsg error(){
        return ERROR;
    }

    public static ApiMsg error(String message){
        ApiMsg msg = new ApiMsg(200, "error");
        msg.addMessage(message);
        return msg;
    }

    public static ApiMsg ok(Map<String,Object> data){
        ApiMsg msg = new ApiMsg(ConditionType.OK);
        msg.setData(data);
        return msg;
    }

    public static ApiMsg ok(){
        return ok(null);
    }

    public static ApiMsg notLogged(){
        return new ApiMsg(ConditionType.UNAUTHORIZED);
    }

    public static ApiMsg notFound(){
        return new ApiMsg(ConditionType.NOT_FOUND);
    }

    public static ApiMsg serveException(){
        return new ApiMsg(ConditionType.SERVER_ERROR);
    }

    public static ApiMsg serveException(String message){
        ApiMsg msg = new ApiMsg(ConditionType.SERVER_ERROR);
        msg.addMessage(message);
        return msg;
    }

    /**
     * warn:不可对返回值进行更改
     */
    public static ApiMsg notLogin() {
        return NOT_LOGIN;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        if (this == ERROR || this == NOT_LOGIN) {
            throw new ParamChangeException("试图向ApiMsg类中的缓存添加数据");
        }
        this.data = data;
    }

    public void addMessage(String message){
        setMessage(this.message + " : " + message);
    }

    public void setMessage(String message){
        if (this == ERROR || this == NOT_LOGIN) {
            throw new ParamChangeException("试图修改ApiMsg类中的缓存信息");
        }
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
