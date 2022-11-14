package com.example.team_project.dao.impl;

import com.example.team_project.dao.SportAnswerDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.SportAnswer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Bean("sportAnswerDao")
public class SportAnswerDaoImpl extends AbsBaseMapper implements SportAnswerDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(SportQuestionDaoImpl.class.getClassLoader().getResourceAsStream("sql/sport-question-and-answer.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SportAnswer> getAnswersByPage(String question, int current, int count) {
        SportAnswer answer = new SportAnswer();
        answer.setQuestion(question);
        return select(answer, current, count);
    }

    @Override
    public List<SportAnswer> getAllAnswers(String question) {
        SportAnswer answer = new SportAnswer();
        answer.setQuestion(question);
        return select(answer);
    }

    @Override
    public int reply(SportAnswer answer) throws SQLException {
        return insert(answer);
    }

    @Override
    public int delReply(String answerId) throws SQLException {
        SportAnswer answer = new SportAnswer();
        answer.setUuid(answerId);
        return delete(answer);
    }

    @Override
    public long countOfAnswer(String question) {
        SportAnswer answer = new SportAnswer();
        answer.setQuestion(question);
        return count(answer);
    }


    @Override
    public String getTableName() {
        return "sport_answer";
    }
}
