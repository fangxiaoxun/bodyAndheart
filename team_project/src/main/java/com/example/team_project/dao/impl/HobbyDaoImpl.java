package com.example.team_project.dao.impl;

import com.example.team_project.dao.HobbyDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Hobby;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * hobby的数据层
 */
public class HobbyDaoImpl extends AbsBaseMapper implements HobbyDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(HobbyDaoImpl.class.getClassLoader().getResourceAsStream("sql/hobby.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Hobby> all() {
        return select(new Hobby());
    }

    @Override
    public List<Hobby> all(int current, int count) {
        String sql = SQL.getProperty("allByPage");
        return jdbcUtils.query(sql,Hobby.class,current,count);
    }

    @Override
    public int add(Hobby hobby) throws SQLException {
        return insert(hobby);
    }

    @Override
    public int delete(int hobby) throws SQLException {
        Hobby del = new Hobby();
        del.setHobby(hobby);
        return delete(del);
    }

    @Override
    public String getTableName() {
        return "hobby";
    }
}
