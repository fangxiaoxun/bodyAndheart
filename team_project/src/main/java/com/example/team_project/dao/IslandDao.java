package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Icon;
import com.example.team_project.pojo.Island;

import java.sql.SQLException;
import java.util.List;

public interface IslandDao extends BaseMapper {
    /**
     * 通过岛的相关参数，创建岛
     * @param belong
     * @param name
     * @param introduce
     * @param type
     * @param iconMark
     * @return 影响的条数
     */
    int createIsland(long belong, String name, String introduce, byte type, int iconMark) throws SQLException;

    /**
     * 通过mark获取岛的图片
     * @param iconMark
     * @return 图片的详细信息,如果找不到则返回null
     */
    Icon getByMark(int iconMark);
    /**
     * 获取所有的头像
     * @param current 当前页面
     * @param count 一次展示的页数
     * @return 所有的头像
     */
    List<Icon> getAll(int current, int count);

    /**
     * 同getAll()但不分页
     * @return 所有的头像
     */
    List<Icon> getAll();

    /**
     * 设置用户头像
     * @param belong 设置图片的岛的id
     * @param iconMark 要设置的图片
     * @return 设置成功或失败,0为失败
     */
    int setIcon(long belong, int iconMark) throws SQLException;

    /**
     * 某位用户是否有岛,也可用于展示岛
     * @param userId 用户的id
     * @return 岛的相关信息，如果没有岛，则返回null
     */
    Island hasIsland(long userId);

    long getMaxIid();

    long getMinIid();

    /**
     * 改变用户的岛的信息
     * @param belong
     * @param name
     * @param introduce
     * @param type
     * @param iconMark
     * @return
     */
    int change(long belong, String name, String introduce, byte type, int iconMark) throws SQLException;

    /**
     * 获取多个岛的信息，有多少个belong就传回多少个
     * @param belong 岛的id
     * @return 如果一个参数没有传，则返回null，如果有传则不会为null
     */
    List<Island> getIslandByIds(long... belong);

    /**
     * 分页获取岛id
     * @param current 当前页数
     * @param count 一次给的条数
     */
    long[] getIslandsIdByPage(int current, int count);

    /**
     * 获得所有岛的数量
     * @return 岛的数量
     */
    long islandsCount();

    /**
     * 获取岛的置顶文章
     * @param belong 岛的id
     * @return 置顶文章的id,无则为0
     */
    long getTopPost(long belong);

    /**
     * 更改置顶动态
     * @param belong 要更改置顶的岛id
     * @param pid 要更改为置顶的动态
     * @return 成功则返回大于0的值
     */
    int changeTopPost(long belong, Long pid) throws SQLException;

    /**
     * 设置指定动态为null
     * @param belong 要设置的岛
     * @return 影响的条数
     * @throws SQLException
     */
    int setNullTopPost(long belong) throws SQLException;

    /**
     * 获取曾经过去的size个岛的id（浏览记录）
     * @param uid 要查看的用户
     * @param size 个数
     * @return 岛的id
     */
    List<Long> getHasBeen(long uid, int size);

    /**
     * 获取曾经去过的岛的个数
     * @param uid 要查看的用户
     * @return 个数
     */
    long beenCount(long uid);

    /**
     * 添加一条浏览记录
     * @param uid 要添加浏览记录的用户
     * @param hasBeen 要被添加浏览记录的岛
     */
    int addRecord(long uid, long hasBeen) throws SQLException;

    int delRecord(long uid, long hasBeen) throws SQLException;
}
