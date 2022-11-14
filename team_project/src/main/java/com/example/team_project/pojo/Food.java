package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Food {
    private String foodName;
    @Param("img")
    private String iconUrl;
    /**
     * 食部
     */
    private Float edible;
    /**
     * 热量，以千卡为单位
     */
    private Float energy;
    private Float protein;
    private Float fat;
    private Float cho;
    private Float dietaryFiber;
    /**
     * 用来推导食物的分组的属性
     */
    @JsonIgnore
    private Integer detailed;

    @Param(value = "foodGroup",ignore = true)
    private String foodGroup;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Float getEdible() {
        return edible;
    }

    public void setEdible(Float edible) {
        this.edible = edible;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }

    public Float getProtein() {
        return protein;
    }

    public void setProtein(Float protein) {
        this.protein = protein;
    }

    public Float getCho() {
        return cho;
    }

    public Float getFat() {
        return fat;
    }

    public void setFat(Float fat) {
        this.fat = fat;
    }

    public void setCho(Float cho) {
        this.cho = cho;
    }

    public Float getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(Float dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getDetailed() {
        return detailed;
    }

    public void setDetailed(Integer detailed) {
        this.detailed = detailed;
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodName='" + foodName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", edible=" + edible +
                ", energy=" + energy +
                ", protein=" + protein +
                ", fat=" + fat +
                ", cho=" + cho +
                ", dietaryFiber=" + dietaryFiber +
                ", detailed=" + detailed +
                ", foodGroup='" + foodGroup + '\'' +
                '}';
    }
}
