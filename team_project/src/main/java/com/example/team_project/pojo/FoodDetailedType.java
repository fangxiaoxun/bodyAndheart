package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

/**
 * 食物的小分类
 */
public class FoodDetailedType {
    private Integer id;
    @Param("type")
    private String typeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "FoodDetailedType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
