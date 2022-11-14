package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Question;

import java.sql.SQLException;
import java.util.List;

public interface SecurityDao extends BaseMapper {
    /**
     * 通过user的id获取用户有的密保问题
     * @param userId
     * @return 返回用户有的密保问题列表
     */
    List<Question> hasQuestions(long userId);

    /**
     * 对比用户输入的密保答案与库中数据是否相同
     * @param question 密保问题的id
     * @param answer 用户的回答
     * @return 相同返回true，不同返回false，异常返回false
     */
    boolean compare(long user,int question, String answer);

    /**
     * 添加密保问题
     * @param question 问题的id
     * @param answer 问题的答案
     * @return 返回是否添加成功
     */
    boolean addQuestion(long user, int question, String answer) throws SQLException;

    /**
     * 获取所有问题
     * @return 密保问题的集合
     */
    List<Question> all();

}
