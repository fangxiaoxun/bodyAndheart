package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

import java.io.Serializable;

/**
 * 用户头像信息
 */
public class Icon implements Serializable {
    @Param("icon_mark")
    private Integer iconMark;
    @Param("icon_url")
    private String iconUrl;

    public int getIconMark() {
        return iconMark;
    }

    public void setIconMark(int iconMark) {
        this.iconMark = iconMark;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "Icon{" +
                "iconMark=" + iconMark +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
