package com.example.team_project.framkwork.comment.emun;

/**
 * 响应状态信息枚举
 */
public enum ConditionType {

    OK(200,"OK"),

    BAD_REQUEST(400, "请求报文语法错误[参数校验失败]"),
    UNAUTHORIZED(401,"未认证身份"),
    FORBIDDEN_REQUEST(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),

    SERVER_ERROR(500, "服务运行异常"),
    SERVICE_UN_AVAILABLE(503, "服务不可用"),
    SERVER_BUSY(505, "服务繁忙"),

    MISSING_PARAM(4001, "参数缺失"),
    ERROR_PARAM(4002, "参数不合法"),
    EXIST(4003, "目标已存在"),
    MISMATCH(4004, "信息不匹配"),
    REQUEST_UNSUPPORTED(4005, "请求不支持");




    private int code;
    private String msg;

    ConditionType(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
