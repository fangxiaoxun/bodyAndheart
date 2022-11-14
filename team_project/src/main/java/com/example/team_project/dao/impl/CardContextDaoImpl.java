package com.example.team_project.dao.impl;

import com.example.team_project.dao.CardContextDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.CardContext;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Bean("cardContextDao")
public class CardContextDaoImpl extends AbsBaseMapper implements CardContextDao {
    /**
     * 存放着所有卡的信息
     */
    private final List<CardContext> cards;

    /**
     * 在bean实例化的时候触发，会初始化cards，减少数据库访问次数
     */
    public CardContextDaoImpl(){
        //获取一个不可变的卡片数组，防止卡片内容被更改
        cards = Collections.unmodifiableList(select(new CardContext()));
    }

    @Override
    public CardContext oneCard() {
        Random random = new Random();
        //在cards中返回一个值
        return cards.get(random.nextInt(cards.size()));
    }

    @Override
    public List<CardContext> allCard() {
        return cards;
    }

    @Override
    public String getTableName() {
        return "card_context";
    }
}
