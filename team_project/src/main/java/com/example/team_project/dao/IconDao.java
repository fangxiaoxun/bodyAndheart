package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Icon;

import java.sql.SQLException;
import java.util.List;

/**
 * icon实体类相关的数据层操作
 */
public interface IconDao extends BaseMapper {
    /**
     * 获取所有的头像
     * @param current 当前页面
     * @param count 一次展示的页数
     * @return 所有的头像
     */
    List<Icon> getAll(int current, int count);

    /**
     * 所有头像的数量
     * @return 所有头像的数量
     */
    long getCount();

    /**
     * 设置用户头像
     * @param userId 设置头像的用户的id
     * @param iconMark 要设置的头像
     * @return 设置成功或失败,0为失败
     */
    int setIcon(long userId, int iconMark) throws SQLException;
}
