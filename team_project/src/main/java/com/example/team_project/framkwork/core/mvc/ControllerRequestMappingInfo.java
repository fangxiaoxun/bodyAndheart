package com.example.team_project.framkwork.core.mvc;

import com.example.team_project.framkwork.comment.emun.RequestType;

import java.lang.reflect.Method;

/**
 * Controller的请求信息
 */
public class ControllerRequestMappingInfo {
    /**
     * 相当与RequestMapping注解的name
     */
    private String requestName;
    /**
     * 访问路径，若注解没有写这个，则为name
     */
    private String[] urlPattern;
    /**
     * 优先级
     */
    private int priority;
    /**
     * 请求方法（get、post等）
     */
    private RequestType[] types;
    /**
     * 接口方法，便于用反射来调用
     */
    private Method method;

    public ControllerRequestMappingInfo(String requestName, String[] urlPattern, int priority, RequestType[] types) {
        this.requestName = requestName;
        this.urlPattern = urlPattern;
        this.priority = priority;
        this.types = types;
    }

    public ControllerRequestMappingInfo(String requestName, String[] urlPattern, int priority, RequestType[] types, Method method) {
        this.requestName = requestName;
        this.urlPattern = urlPattern;
        this.priority = priority;
        this.types = types;
        this.method = method;
    }

    public ControllerRequestMappingInfo(String[] urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String[] getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String[] urlPattern) {
        this.urlPattern = urlPattern;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RequestType[] getTypes() {
        return types;
    }

    public void setTypes(RequestType[] types) {
        this.types = types;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
