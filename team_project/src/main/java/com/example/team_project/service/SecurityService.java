package com.example.team_project.service;


import com.example.team_project.pojo.Question;

import java.util.List;

public interface SecurityService {
    /**
     * 得到某个用户的所有密保问题
     * @param user
     * @return 密保问题列表，只会为空内容，非null
     */
    List<Question> getQuestions(long user);

    /**
     * 比较密保问题是否正确
     * @param user 用户id
     * @param question 问题id
     * @param answer 回答的内容
     * @return 是否正确
     */
    boolean compare(long user, int question, String answer);

    /**
     * 添加密保问题
     * @return 是否添加成功
     */
    boolean add(long user, int question, String answer);

    /**
     * 获取所有密保问题
     * @return 所有密保问题
     */
    List<Question> all();

    /**
     * 找到用户没有的所有密保问题
     * @return 没有的密保问题集合
     */
    List<Question> findNoHave(long user);
}
