package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Island {
    @Param("belong")
    private Long belong;
    @Param("name")
    private String name;
    @Param("intro")
    private String intro;
    @Param("type")
    private Integer type;
    /**
     * 人口数，也为关注数
     */
    @Param(value = "population",ignore = true)
    private Long population;
    @Param("icon_mark")
    private Integer iconMark;
    /**
     * 该属性用于获取岛内的指定动态，及其内容
     */
    @JsonIgnore
    @Param("top_post")
    private Long topPost;

    public Long getBelong() {
        return belong;
    }

    public void setBelong(Long belong) {
        this.belong = belong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

//    public int getMark() {
//        return mark;
//    }
//


    public int getIconMark() {
        return iconMark;
    }

    public void setIconMark(int iconMark) {
        this.iconMark = iconMark;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

//    public void setMark(Integer mark) {
//        this.mark = mark;
//    }

    public void setIconMark(Integer iconMark) {
        this.iconMark = iconMark;
    }

    public Long getTopPost() {
        return topPost;
    }

    public void setTopPost(Long topPost) {
        this.topPost = topPost;
    }

    @Override
    public String toString() {
        return "Island{" +
                "belong=" + belong +
                ", name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", type=" + type +
                ", population=" + population +
                ", iconMark=" + iconMark +
                ", topPost=" + topPost +
                '}';
    }
}
