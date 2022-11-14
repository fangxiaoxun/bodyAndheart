package com.example.team_project.framkwork.core.mvc;

import java.util.List;

public class ControllerInfo<T> {
    /**
     * 也是保存在BeanFactory中的bean名称(key)
     */
    private String controllerName;
    /**
     * Controller的类对象
     */
    private Class<T> controllerClass;
    /**
     * Controller类本身的API信息
     */
    private ControllerRequestMappingInfo controllerInfo;
    /**
     * Controller中所有携带RequestMapping注解的方法的信息
     */
    private List<ControllerRequestMappingInfo> methodRequestMappingInfo;

    public ControllerInfo(String controllerName, Class<T> controllerClass,
                          ControllerRequestMappingInfo controllerInfo,
                          List<ControllerRequestMappingInfo> methodRequestMappingInfo) {
        this.controllerName = controllerName;
        this.controllerClass = controllerClass;
        this.controllerInfo = controllerInfo;
        this.methodRequestMappingInfo = methodRequestMappingInfo;
    }

    public ControllerInfo() {
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public Class<T> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<T> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public ControllerRequestMappingInfo getControllerInfo() {
        return controllerInfo;
    }

    public void setControllerInfo(ControllerRequestMappingInfo controllerInfo) {
        this.controllerInfo = controllerInfo;
    }

    public List<ControllerRequestMappingInfo> getMethodRequestMappingInfo() {
        return methodRequestMappingInfo;
    }

    public void setMethodRequestMappingInfo(List<ControllerRequestMappingInfo> methodRequestMappingInfo) {
        this.methodRequestMappingInfo = methodRequestMappingInfo;
    }
}
