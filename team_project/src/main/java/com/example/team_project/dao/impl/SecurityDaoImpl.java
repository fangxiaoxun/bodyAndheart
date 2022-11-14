package com.example.team_project.dao.impl;

import com.example.team_project.dao.SecurityDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Question;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SecurityDaoImpl extends AbsBaseMapper implements SecurityDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(SecurityDaoImpl.class.getClassLoader().getResourceAsStream("sql/security.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> hasQuestions(long userId) {
        return jdbcUtils.query(SQL.getProperty("userHave"), Question.class, userId);
    }

    @Override
    public boolean compare(long user, int question, String answer) {
        Question po = new Question();
        po.setUserId(user);
        po.setQuestion(question);
        po.setAnswer(answer);
        Question query = selectOne(po);
        return query != null;
    }

    @Override
    public boolean addQuestion(long user, int question, String answer) throws SQLException {
        Question po = new Question();
        po.setUserId(user);
        po.setQuestion(question);
        po.setAnswer(answer);
        int insert = insert(po);
        return insert != 0;
    }

    @Override
    public List<Question> all() {
        String sql = SQL.getProperty("all");
        List<Question> all = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcUtils.queryForList(sql);
        for (Map<String, Object> map : maps) {
            Question question = new Question();
            question.setQuestion((Integer) map.get("question"));
            question.setDescribe((String) map.get("describe"));
            all.add(question);
        }
        return all;
    }

    @Override
    public String getTableName() {
        return "security";
    }
}
