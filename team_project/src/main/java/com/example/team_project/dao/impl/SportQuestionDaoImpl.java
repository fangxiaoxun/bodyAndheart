package com.example.team_project.dao.impl;

import com.example.team_project.dao.SportQuestionDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.SportQuestion;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Bean("sportQuestionDao")
public class SportQuestionDaoImpl extends AbsBaseMapper implements SportQuestionDao {
    /**
     * 提问的缓存
     */
    private static final Map<String, SportQuestion> sportQuestions = new ConcurrentHashMap<>();
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(SportQuestionDaoImpl.class.getClassLoader().getResourceAsStream("sql/sport-question-and-answer.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SportQuestionDaoImpl() {
        //初始化提问，减少后续对于数据库的访问
        String sql = SQL.getProperty("getQuestions");
        List<Map<String, Object>> queryForList = jdbcUtils.queryForList(sql);
        for (Map<String, Object> map : queryForList) {
            String questionId = (String) map.get("question");
            //通过map中的数据创建一个对象
            SportQuestion sportQuestion = MapUtils.instanceByMap(SportQuestion.class, map);
            //若map中不存在该对象，则放入
            sportQuestions.putIfAbsent(questionId,sportQuestion);
        }
    }

    @Override
    public List<SportQuestion> getQuestionsRandom(int size) {
        List<SportQuestion> questions;
        //获取文章的最大篇数
        int count = sportQuestions.size();
        if (size >= count) {
            //把所有的提问都返回
            questions = new ArrayList<>(sportQuestions.values());
        }else {
            //随机获得count个不重复数
            Set<Integer> indexes = new HashSet<>();
            while (indexes.size() < size) {
                indexes.add((int) (Math.random() * count));
            }
            Object[] objects = sportQuestions.values().toArray();
            questions = new ArrayList<>(size);
            //存入count个不重复提问
            for (int index : indexes) {
                questions.add((SportQuestion) objects[index]);
            }
        }
        return questions;
    }

    @Override
    public int releaseQuestion(SportQuestion question) throws SQLException {
        question.setqTime(LocalDateTime.now());
        int insert = insert(question);
        if (insert != 0) {
            sportQuestions.put(question.getQuestion(), question);
        }
        return insert;
    }

    @Override
    public int delQuestion(String uuid) throws SQLException {
        SportQuestion question = new SportQuestion();
        question.setQuestion(uuid);
        int delete = delete(question);
        if (delete != 0) {
            sportQuestions.remove(uuid);
        }
        return delete;
    }

    @Override
    public String getTableName() {
        return "sport_question";
    }

}
