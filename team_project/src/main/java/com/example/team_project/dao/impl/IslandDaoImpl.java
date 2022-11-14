package com.example.team_project.dao.impl;

import com.example.team_project.dao.IslandDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Icon;
import com.example.team_project.pojo.Island;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class IslandDaoImpl extends AbsBaseMapper implements IslandDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(IslandDaoImpl.class.getClassLoader().getResourceAsStream("sql/island.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getIslandCount() {
        String sql = SQL.getProperty("islandCount");
        return (long) jdbcUtils.queryForMap(sql).get("count");
    }

    @Override
    public int createIsland(long belong, String name, String introduce, byte type, int iconMark) throws SQLException {
        String sql = SQL.getProperty("create");
        return jdbcUtils.update(sql, belong, name, introduce, type, iconMark);
    }

    @Override
    public Icon getByMark(int iconMark) {
        Icon icon = new Icon();
        icon.setIconMark(iconMark);
        return selectOne(icon);
    }

    @Override
    public List<Icon> getAll(int current, int count) {
        String sql = SQL.getProperty("getAllIslandIconByPage");
        return jdbcUtils.query(sql, Icon.class, current, count);
    }

    @Override
    public List<Icon> getAll() {
        String sql = SQL.getProperty("getAllIslandIcon");
        return jdbcUtils.query(sql, Icon.class);
    }

    @Override
    public int setIcon(long belong, int iconMark) throws SQLException {
        String sql = SQL.getProperty("setIslandIcon");
        return jdbcUtils.update(sql, iconMark, belong);
    }

    @Override
    public Island hasIsland(long userId) {
        String sql = SQL.getProperty("hasIsland");
        Map<String, Object> param = jdbcUtils.queryForMap(sql, userId);
        return MapUtils.instanceByMap(Island.class, param);
    }

    @Override
    public long getMaxIid() {
        String sql = SQL.getProperty("getMaxIid");
        Object belong = jdbcUtils.queryForMap(sql).get("belong");
        return belong == null ? 0 : (long) belong;
    }

    @Override
    public long getMinIid() {
        String sql = SQL.getProperty("getMinIid");
        Object belong = jdbcUtils.queryForMap(sql).get("belong");
        return belong == null ? 0 : (long) belong;
    }

    @Override
    public int change(long belong, String name, String introduce, byte type, int iconMark) throws SQLException {
        String sql = SQL.getProperty("changeInfo");
        return jdbcUtils.update(sql, name, introduce, type, iconMark, belong);
    }

    @Override
    public List<Island> getIslandByIds(long... belong) {
        List<Island> islands = null;
        //必须是belong不为空且大小不为0,才能进行操作
        if (belong != null && belong.length > 0) {
            StringBuilder sql = new StringBuilder("SELECT *FROM island WHERE ");
            //对第一个特殊处理
            sql.append("belong = ").append(belong[0]);
            for (int i = 1; i < belong.length; i++) {
                sql.append(" OR belong = ").append(belong[i]);
            }
            islands = jdbcUtils.query(sql.toString(), Island.class);
        }
        return islands;
    }

    @Override
    public long[] getIslandsIdByPage(int current, int count) {
        long[] ids = null;
        String sql = SQL.getProperty("getIslandsIdByPage");
        List<Map<String, Object>> maps = jdbcUtils.queryForList(sql, current, count);
        if (maps.size() > 0) {
            ids = new long[maps.size()];
            for (int i = 0; i < maps.size(); i++) {
                ids[i] = (long) maps.get(i).get("belong");
            }
        }
        return ids;
    }

    @Override
    public long islandsCount() {
        String sql = SQL.getProperty("islandsCount");
        Map<String, Object> map = jdbcUtils.queryForMap(sql);
        return (long) map.get("count");
    }

    @Override
    public long getTopPost(long belong) {
        String sql = SQL.getProperty("getTopPost");
        Object id = jdbcUtils.queryForMap(sql, belong).get("id");
        if (id == null) {
            return 0;
        }
        return (long) id;
    }

    @Override
    public int changeTopPost(long belong, Long pid) throws SQLException {
        String sql = SQL.getProperty("changeTopPost");
        return jdbcUtils.update(sql, pid, belong);
    }

    @Override
    public int setNullTopPost(long belong) throws SQLException {
        String sql = SQL.getProperty("setNullTopPost");
        return jdbcUtils.update(sql, belong);
    }

    @Override
    public List<Long> getHasBeen(long uid, int size) {
        String sql = SQL.getProperty("getHasBeen");
        List<Map<String, Object>> maps = jdbcUtils.queryForList(sql, uid, size);
        List<Long> result = new ArrayList<>(size);
        //遍历集合，获取其中的岛id
        for (Map<String, Object> map : maps) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                result.add((Long) entry.getValue());
            }
        }
        return result;
    }

    @Override
    public long beenCount(long uid) {
        String sql = SQL.getProperty("beenCount");
        Map<String, Object> map = jdbcUtils.queryForMap(sql, uid);
        return (long) map.get("count");
    }

    @Override
    public int addRecord(long uid, long hasBeen) throws SQLException {
        String sql = SQL.getProperty("addRecord");
        return jdbcUtils.insert(sql, uid, hasBeen);
    }

    @Override
    public int delRecord(long uid, long hasBeen) throws SQLException {
        String sql = SQL.getProperty("deleteRecord");
        return jdbcUtils.delete(sql, uid, hasBeen);
    }

    @Override
    public String getTableName() {
        return "island_icon";
    }
}
