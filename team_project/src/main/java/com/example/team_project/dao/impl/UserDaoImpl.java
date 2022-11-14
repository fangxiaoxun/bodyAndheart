package com.example.team_project.dao.impl;

import com.example.team_project.dao.UserDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
@Bean("userDao")
public class UserDaoImpl extends AbsBaseMapper implements UserDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(UserDaoImpl.class.getClassLoader().getResourceAsStream("sql/user.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(long id, String password, boolean byCookie) {
        String sql;
        if (byCookie){
            sql = SQL.getProperty("loginByCookie");
        }else {
            sql = SQL.getProperty("login");
        }
        return MapUtils.instanceByMap(User.class,jdbcUtils.queryForMap(sql,id,password));
    }

    @Override
    public int signIn(String password, int iconMark) throws SQLException {
        Long id = null;
        String sql = SQL.getProperty("register");
        int result = 0;
        result = jdbcUtils.insert(sql, password, iconMark);
        return result;
    }

    @Override
    public Long getIdAfterSignIn(int signInResult) {
        Long id = null;
        if (signInResult != 0) {
            String sql = SQL.getProperty("max");
            Map<String, Object> query = jdbcUtils.queryForMap(sql);
            id = (Long) query.get("max");
        }
        return id;
    }

    @Override
    public boolean changePassword(long id, String password) throws SQLException {
        boolean result = false;
        String sql = SQL.getProperty("changePassword");
        int update = jdbcUtils.update(sql, password, id);
        if (update != 0){
            result = true;
        }
        return result;
    }

    @Override
    public String getTableName() {
        return "user";
    }

}
