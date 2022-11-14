package com.example.team_project.pojo;

/**
 * 卡片的内容
 */
public class CardContext {
    private String theme;
    private String desc;
    private String body;

    public CardContext(String theme, String desc, String body) {
        this.theme = theme;
        this.desc = desc;
        this.body = body;
    }

    public CardContext() {
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "CardContext{" +
                "theme='" + theme + '\'' +
                ", desc='" + desc + '\'' +
                ", body='" + body + '\'' +
                '}';
    }


}
