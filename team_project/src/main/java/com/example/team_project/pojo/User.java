package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.example.team_project.utils.ServiceConfUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.io.Serializable;

/**
 * pojo的数据需要全部为包装类
 */
public class User implements Serializable {
    @Param("id")
    private Long id;
    @Param("password")
    private String password;
    @Param("icon_mark")
    private Integer iconMark;
    /**
     * 用户所拥有的积分, 不一定会做这个业务
     */
    private Integer point;
    /**
     * 头像直接给会更合适，但限于原设计已经做了多数正在使用的接口，故不改动
     */
    @Param(value = "url", ignore = true)
    private String iconUrl;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIconMark() {
        return iconMark;
    }

    public void setIconMark(int iconMark) {
        this.iconMark = iconMark;
    }

    public void setIconMark(Integer iconMark) {
        this.iconMark = iconMark;
    }

    public String getIconUrl() {
        String host = ServiceConfUtils.getConfig("HOST");
        String contextPath = ServiceConfUtils.getConfig(ServiceConfUtils.CONTEXT_PATH);
        return host + contextPath + iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", iconMark=" + iconMark +
                ", point=" + point +
                '}';
    }
}
