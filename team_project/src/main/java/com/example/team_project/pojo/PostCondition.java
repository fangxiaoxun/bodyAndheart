package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

public class PostCondition {
    @Param("post_condition")
    private Integer condition;
    @Param("describe")
    private String describe;
    @Param("url")
    private String url;


    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PostCondition{" +
                "condition=" + condition +
                ", describe='" + describe + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
