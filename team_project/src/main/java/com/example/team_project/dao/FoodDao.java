package com.example.team_project.dao;

import com.example.team_project.pojo.Food;
import com.example.team_project.pojo.FoodGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FoodDao {
    /**
     * 获取食物信息
     * @param fid 食物的id
     * @return 食物的信息数据，为null则食物不存在
     */
    Food getDataById(int fid);

    /**
     * 排列的Food信息，同一分组内进行排序，剩下的自定义排序，默认升序
     * @param orderBy 自定义排序，要什么字段排序，orderBy中就需要有什么数据
     * @param foodGroup 要排序的分组
     * @param orderReverse 是否要逆序
     * @return 分组内排序后的食物，集合不为null
     */
    List<Food> orderedList(Set<String> orderBy, int foodGroup, boolean orderReverse);

    /**
     * 通过detailed找到对应的大组别
     * @param detailed 食物的详细组别
     * @return 返回大组别的名字
     */
    String findGroup(int detailed);

    /**
     * 获取所有的分组信息
     * @return List集合中存放着所有的分组信息，包括小分组。如果没有长度为0
     */
    List<FoodGroup> allGroup();

    /**
     * 通过detailed的id来获取这个小类下的所有的食物信息
     * @param detailedId 小类的id（唯一标识）
     * @return 属于detailedId的所有的食物信息，若不存在，则为null
     */
    List<Food> getFoodsByDetailed(int detailedId);

    /**
     * 简单的搜索
     * @param nameCondition 要搜索的食物的名字（名字的部分）
     * @return 符合条件的食物
     */
    List<Food> simpleSearch(String nameCondition);

    /**
     * 通过小类来获取大类id
     * @param detailId 小类id
     * @return 大类id
     */
    int getGroupIdByDetailId(int detailId);

}
