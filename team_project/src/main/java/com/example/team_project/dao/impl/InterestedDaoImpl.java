package com.example.team_project.dao.impl;

import com.example.team_project.dao.InterestedDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Interested;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class InterestedDaoImpl extends AbsBaseMapper implements InterestedDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(InterestedDaoImpl.class.getClassLoader().getResourceAsStream("sql/hobby.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Interested> userHas(long userId) {
        Interested condition = new Interested();
        condition.setUser(userId);
        return select(condition);
    }

    @Override
    public List<Interested> userHas(long userId, int current, int count) {
        String sql = SQL.getProperty("userHasByPage");
        return jdbcUtils.query(sql,Interested.class,current,count);
    }

    @Override
    public int addInterests(long userId, int[] hobbies) throws SQLException {
        Interested condition = new Interested();
        condition.setUser(userId);
        int result = 0;
        //一个一个去添加，一个失败全部失败
        for (int hobby : hobbies) {
            if (hobby != 0) {
                condition.setHobby(hobby);
                result = insert(condition);
            }
            if (result == 0) {
                break;
            }
        }
        return result;
    }

    @Override
    public int delInterests(long userId, int[] deletes) throws SQLException {
        Interested condition = new Interested();
        condition.setUser(userId);
        int result = 0;
        //一个一个删除，若有一个失败，则全部失败
        for (int delete : deletes) {
            if (delete != 0) {
                condition.setHobby(delete);
                result = delete(condition);
            }
            if (result == 0){
                break;
            }
        }
        return result;
    }

    @Override
    public String getTableName() {
        return "interested";
    }
}
