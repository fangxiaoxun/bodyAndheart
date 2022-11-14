package com.example.team_project.dao.impl;

import com.example.team_project.dao.IconDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Icon;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class IconDaoImpl extends AbsBaseMapper implements IconDao {
    private static final Properties pro = new Properties();

    static {
        try {
            pro.load(IconDaoImpl.class.getClassLoader().getResourceAsStream("sql/icon.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTableName() {
        return "icon";
    }

    @Override
    public List<Icon> getAll(int current, int count) {
        String sql = pro.getProperty("getAll");
        return jdbcUtils.query(sql,Icon.class,current,count);
    }

    @Override
    public long getCount() {
        Map<String, Object> map = jdbcUtils.queryForMap(pro.getProperty("getCount"));
        long result = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result = (Long) entry.getValue();
        }
        return result;
    }

    @Override
    public int setIcon(long userId, int iconMark) throws SQLException {
        String sql = pro.getProperty("setIcon");
        return jdbcUtils.update(sql,iconMark,userId);
    }

}
