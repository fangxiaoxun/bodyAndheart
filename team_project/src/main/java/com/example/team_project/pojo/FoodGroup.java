package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

import java.util.List;

/**
 * 食物的大分类
 */
public class FoodGroup {
    private Integer id;
    /**
     * 食物的大组名
     */
    private String group;
    /**
     * 对应的图片
     */
    private String img;
    @Param(value = "detailed",ignore = true)
    private List<FoodDetailedType> detailed;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<FoodDetailedType> getDetailed() {
        return detailed;
    }

    public void setDetailed(List<FoodDetailedType> detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "FoodGroup{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", detailed=" + detailed +
                '}';
    }
}
