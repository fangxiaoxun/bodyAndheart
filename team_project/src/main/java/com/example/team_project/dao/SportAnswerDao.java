package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.SportAnswer;

import java.sql.SQLException;
import java.util.List;

public interface SportAnswerDao extends BaseMapper {
    /**
     * 分页获取回复
     * @param question 要获取回复的提问的唯一标识
     * @param current 当前页码
     * @param count 一次显示多少条
     * @return 回复的集合
     */
    List<SportAnswer> getAnswersByPage(String question, int current, int count);

    /**
     * 获取所有的回答
     * @param question 要获取回答的问题的唯一标识
     * @return 回答的集合
     */
    List<SportAnswer> getAllAnswers(String question);

    /**
     * 回复问题
     * @param answer 回复的相关内容
     * @return 影响的条目数
     * @throws SQLException
     */
    int reply(SportAnswer answer) throws SQLException;

    /**
     * 删除回答
     * @param answerId 回复的唯一标识
     * @return 影响的条目数
     * @throws SQLException
     */
    int delReply(String answerId) throws SQLException;

    /**
     * 查询某个问题的回答条数
     * @param question 问题的唯一标识
     * @return 回答条数
     */
    long countOfAnswer(String question);

}
