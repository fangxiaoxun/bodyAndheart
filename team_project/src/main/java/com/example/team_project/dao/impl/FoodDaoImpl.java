package com.example.team_project.dao.impl;

import com.example.team_project.dao.FoodDao;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.jdbc.JDBCUtils;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Food;
import com.example.team_project.pojo.FoodDetailedType;
import com.example.team_project.pojo.FoodGroup;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

/**
 * 与食物相关的数据层，由于此处较多为多表查询，故不继承AbsBaseMapper
 */
public class FoodDaoImpl implements FoodDao {
    private static final JDBCUtils JDBC_UTILS = new JDBCUtils(BeanFactory.getBean("dataSource", DataSource.class));
    private static final Properties SQL;

    //对Properties进行初始化
    static {
        SQL = new Properties();
        try {
            SQL.load(FoodDaoImpl.class.getClassLoader().getResourceAsStream("sql/food.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Food getDataById(int fid) {
        String sql = SQL.getProperty("getDataById");
        Map<String, Object> map = JDBC_UTILS.queryForMap(sql, fid);
        //封装成对象，如果没有数据（不存在）则返回null
        return MapUtils.instanceByMap(Food.class,map);
    }

    @Override
    public List<Food> orderedList(Set<String> orderBy, int foodGroup, boolean orderReverse) {
        //应该写在文件
        StringBuilder str = new StringBuilder(SQL.getProperty("orderedList"));
        //当且仅当food成员中不为null的值大于0，才能够进行拼接sql语句
        if (orderBy.size() != 0) {
            Iterator<String> iterator = orderBy.iterator();
            //第一个较为特殊，需要特殊处理
            str.append(" ORDER BY ").append(iterator.next());
            while (iterator.hasNext()) {
                str.append(", ").append(iterator.next());
            }
            //降序，只有在正常的状态下，orderReverse才有作用
            if (orderReverse) {
                str.append(" DESC");
            }
        }
        //若set集合的size等于0，则直接进行查询，不用进行排序
        return JDBC_UTILS.query(str.toString(),Food.class,foodGroup);
    }

    @Override
    public List<FoodGroup> allGroup() {
        String groupSql = SQL.getProperty("allGroup");
        //拿到所有的大类信息
        List<FoodGroup> foodGroups = JDBC_UTILS.query(groupSql, FoodGroup.class);
        String typeSql = SQL.getProperty("allType");
        //查询每一个食物大分组包含的小分组，并存放进去
        for (FoodGroup foodGroup : foodGroups) {
            foodGroup.setDetailed(JDBC_UTILS.query(typeSql, FoodDetailedType.class,foodGroup.getId()));
        }
        return foodGroups;
    }

    @Override
    public List<Food> getFoodsByDetailed(int detailedId) {
        String sql = SQL.getProperty("getFoodsByDetailed");
        return JDBC_UTILS.query(sql,Food.class,detailedId);
    }

    @Override
    public List<Food> simpleSearch(String nameCondition) {
        String sql = SQL.getProperty("simpleSearch");
        return JDBC_UTILS.query(sql, Food.class, nameCondition);
    }

    @Override
    public int getGroupIdByDetailId(int detailId) {
        String sql = SQL.getProperty("getGroupIdByDetailId");
        Map<String, Object> map = JDBC_UTILS.queryForMap(sql, detailId);
        return (int) map.get("id");
    }

    @Override
    public String findGroup(int detailed) {
        String sql = SQL.getProperty("findGroup");
        Map<String, Object> map = JDBC_UTILS.queryForMap(sql, detailed);
        return (String) map.get("group");
    }


}
