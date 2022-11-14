package com.example.team_project.framkwork.jdbc.repository;

import java.sql.SQLException;
import java.util.List;

public interface BaseMapper {

    <T> T selectOne(T obj);

    <T> int update(T changedPo, T conditionPo) throws SQLException;

    <T> List<T> select(T obj);

    /**
     * 获取所有符合条件的东西，并包装在List中，
     * @param obj 条件，也代表了返回的类型
     * @param currentPage 当前的页码
     * @param count 一次展示的数量
     * @return 符合条件的对象集合，不为null
     */
    <T> List<T> select(T obj, int currentPage, int count);

    <T> int delete(T obj) throws SQLException;

    <T> int insert(T obj) throws SQLException;

    <T> long count(T obj);
    /**
     * 在更新删除插入操作时，进行提交
     */
    void commit();

    /**
     * 在更新删除插入操作时，进行回退
     */
    void rollback();
}
