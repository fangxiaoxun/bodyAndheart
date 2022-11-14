package com.example.team_project.service;

import com.example.team_project.pojo.Food;
import com.example.team_project.pojo.FoodGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FoodService {
    /**
     * 拿到所有的分组信息，包括大小分组
     * @return 所有的分组信息，没有则长度为0
     */
    List<FoodGroup> allGroup();

    /**
     * 对食物信息进行排序，比较灵活，可以根据传入的food对象具有的信息进行分组
     * @param orderBy 根据什么分组
     * @param group 在哪一个大类中
     * @param orderReverse 是否要逆序输出
     * @return 分组后的食物信息
     */
    List<Food> orderedFoodList(Set<String> orderBy, int group, boolean orderReverse);

    /**
     * 对食物信息进行排序，比较灵活，可以根据传入的food对象具有的信息进行分组，并且是升序排序
     * @param orderBy 根据什么分组
     * @param group 在哪一个大类中
     * @return 分组后的食物信息，升序排序
     */
    List<Food> orderedFoodList(Set<String> orderBy, int group);

    /**
     * 根据食物的id来获取食物
     * @param id 食物的id
     * @return 返回该id对应食物的信息，不存在则为null。若id小于等于0则也返回null
     */
    Food getFoodById(int id);

    /**
     * 通过小类获取小类中所有的食物数据
     * @param detailedId 小类的id（唯一标识）
     * @return 返回小类包含的食物信息，若id小于等于0则返回null
     */
    List<Food> getFoodsByDetailedId(int detailedId);

    /**
     * 简单搜索
     * @param nameCondition 想要搜索的对象的部分名字
     * @return 想要搜索的食物
     */
    List<Map<String, Object>> simpleFoodSearch(String nameCondition);


}
