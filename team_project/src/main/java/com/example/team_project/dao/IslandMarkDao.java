package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.IslandMark;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户关注岛的数据层
 */
public interface IslandMarkDao extends BaseMapper {

    /**
     * 获取关注信息
     * @param uid 用户的id
     * @return 关注消息的集合，若无，则为0长度
     */
    List<IslandMark> getMark(long uid);

    /**
     * 是否关注过某个岛
     * @param mark 关注的信息
     * @return 是否关注过
     */
    boolean hasMark(IslandMark mark);

    /**
     * 岛获取关注的人数
     * @param iid 岛的id
     * @return
     */
    long getMarkCount(long iid);

    /**
     * 关注岛
     * @param mark
     * @return 影响的行数
     */
    int mark(IslandMark mark) throws SQLException;

    /**
     * 删除关注
     * @param mark
     * @return 影响的行数
     */
    int delMark(IslandMark mark) throws SQLException;

    /**
     * 查询用户关注过的岛个数
     * @param uid 要查询的用户
     * @return 关注过的个数（不包括自己的）
     */
    long getUserMarkCount(long uid);

    /**
     * 获取单个用户关注的所有岛的id
     * @param uid 要查询的用户
     * @param current 目前的页数
     * @param count 一次展示的条目数
     * @return 关注过的所有岛的id
     */
    List<Long> getMarkIslandIds(long uid, int current, int count);

    /**
     * 获取单个用户关注的所有岛的id，返回全部
     * @param uid 要查询的用户
     * @return 关注过的所有岛的id
     */
    List<Long> getMarkIslandIds(long uid);

}
