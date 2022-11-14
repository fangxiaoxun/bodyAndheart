package com.example.team_project.service.impl;

import com.example.team_project.dao.SecurityDao;
import com.example.team_project.dao.impl.SecurityDaoImpl;
import com.example.team_project.pojo.Question;
import com.example.team_project.service.SecurityService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecurityServiceImpl implements SecurityService {
    private final SecurityDao SECURITY_DAO = new SecurityDaoImpl();

    @Override
    public List<Question> getQuestions(long user) {
        return SECURITY_DAO.hasQuestions(user);
    }

    @Override
    public boolean compare(long user, int question, String answer) {
        return SECURITY_DAO.compare(user, question, answer);
    }

    @Override
    public boolean add(long user, int question, String answer) {
        boolean success;
        try {
            //若添加不存在的对象，则会被阻塞，要修，只有这样，存到缓存或存到redis或再访问一次数据库
            success = SECURITY_DAO.addQuestion(user, question, answer);
        } catch (Exception e) {
            SECURITY_DAO.rollback();
            success = false;
            e.printStackTrace();
        }finally {
            SECURITY_DAO.commit();
        }
        return success;
    }

    @Override
    public List<Question> all() {
        return SECURITY_DAO.all();
    }

    /**
     * 两次访问数据库获取数据，从中获取用户没有设置的密保
     * @param user 用户id
     * @return 返回没有设置的密保的集合
     */
    @Override
    public List<Question> findNoHave(long user) {
        List<Question> all = all();
        List<Question> questions = getQuestions(user);
        //如果都有了则展示全部
        List<Question> noHave = new ArrayList<>();
        //寻找all中存在且questions中不存在的数据
        for (Question question : all) {
            boolean flag = false;
            for (Question quest : questions) {
                if (quest.equals(question)){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                noHave.add(question);
            }
        }
        return noHave;
    }
}
