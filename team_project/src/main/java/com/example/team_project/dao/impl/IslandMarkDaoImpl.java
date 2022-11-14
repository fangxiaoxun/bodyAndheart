package com.example.team_project.dao.impl;

import com.example.team_project.dao.IslandMarkDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.IslandMark;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class IslandMarkDaoImpl extends AbsBaseMapper implements IslandMarkDao {
    private static final Properties SQL = new Properties();

    static {
        try {
            SQL.load(IslandMarkDaoImpl.class.getClassLoader().getResourceAsStream("sql/island.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<IslandMark> getMark(long uid) {
        IslandMark islandMark = new IslandMark();
        islandMark.setFollower(uid);
        return select(islandMark);
    }

    @Override
    public boolean hasMark(IslandMark mark) {
        IslandMark query = selectOne(mark);
        return query != null;
    }

    @Override
    public long getMarkCount(long iid) {
        IslandMark islandMark = new IslandMark();
        islandMark.setIsland(iid);
        return count(islandMark);
    }

    @Override
    public int mark(IslandMark mark) throws SQLException {
        return insert(mark);
    }

    @Override
    public int delMark(IslandMark mark) throws SQLException {
        return delete(mark);
    }

    @Override
    public long getUserMarkCount(long uid) {
        IslandMark mark = new IslandMark();
        mark.setFollower(uid);
        //获取所有的关注量，但不包括自己的岛，而自己的岛又是默认关注，且不可被取消
        return count(mark) - 1;
    }

    @Override
    public List<Long> getMarkIslandIds(long uid, int current, int count) {
        String sql = SQL.getProperty("getAllMarkIslandByPage");
        List<Map<String, Object>> mapList = jdbcUtils.queryForList(sql, uid, uid, current, count);
        List<Long> islandIds = getIslandIds(mapList);
        //去除自己的岛(若存在)
        islandIds.remove(uid);
        return islandIds;
    }

    private List<Long> getIslandIds(List<Map<String, Object>> mapList) {
        List<Long> ids = new LinkedList<>();
        for (Map<String, Object> map : mapList) {
            ids.add((Long) map.get("island"));
        }

        return ids;
    }

    @Override
    public List<Long> getMarkIslandIds(long uid) {
        String sql = SQL.getProperty("getAllMarkIsland");
        List<Map<String, Object>> maps = jdbcUtils.queryForList(sql, uid, uid);
        List<Long> islandIds = getIslandIds(maps);
        //去除自己的岛
        islandIds.remove(uid);
        return islandIds;
    }

    @Override
    public String getTableName() {
        return "island_mark";
    }
}
