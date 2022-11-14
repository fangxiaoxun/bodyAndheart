package com.example.team_project.service.impl;

import com.example.team_project.dao.FoodDao;
import com.example.team_project.dao.impl.FoodDaoImpl;
import com.example.team_project.pojo.Food;
import com.example.team_project.pojo.FoodGroup;
import com.example.team_project.service.FoodService;

import java.util.*;

public class FoodServiceImpl implements FoodService {
    private final FoodDao foodDao = new FoodDaoImpl();

    @Override
    public List<FoodGroup> allGroup() {
        return foodDao.allGroup();
    }

    @Override
    public List<Food> orderedFoodList(Set<String> orderBy, int group, boolean orderReverse) {
        return foodDao.orderedList(orderBy,group,orderReverse);
    }

    @Override
    public List<Food> orderedFoodList(Set<String> orderBy, int group) {
        return orderedFoodList(orderBy,group,false);
    }

    @Override
    public Food getFoodById(int id) {
        if (id > 0) {
            return foodDao.getDataById(id);
        }else {
            return null;
        }
    }

    @Override
    public List<Food> getFoodsByDetailedId(int detailedId) {
        if (detailedId > 0) {
            return foodDao.getFoodsByDetailed(detailedId);
        }else {
            return null;
        }
    }


    @Override
    public List<Map<String, Object>> simpleFoodSearch(String nameCondition) {
        if (nameCondition != null) {
            //减少空格产生的影响
            nameCondition = nameCondition.trim();
            if (!nameCondition.equals("")) {
                //返回只包含食物部分数据，以及小类的食物信息
                List<Food> simpleFoodList = foodDao.simpleSearch("%" + nameCondition + "%");
                List<Map<String, Object>> foodList = new ArrayList<>(simpleFoodList.size());

                for (Food food : simpleFoodList) {
                    Map<String, Object> params = new HashMap<>(14);
                    //把食物的属性放入map中
                    params.put("foodName" ,food.getFoodName());
                    params.put("cho",food.getCho());
                    params.put("edible",food.getEdible());
                    params.put("detailed",food.getDetailed());
                    params.put("energy", food.getEnergy());
                    params.put("fat",food.getFat());
                    params.put("iconUrl",food.getIconUrl());
                    params.put("protein",food.getProtein());
                    params.put("dietaryFiber",food.getDietaryFiber());
                    params.put("groupId",foodDao.getGroupIdByDetailId(food.getDetailed()));
                    //添加进list集合返回
                    foodList.add(params);
                }
                return foodList;
            }
        }
        return new ArrayList<>(0);
    }
}
