package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.CardContext;

import java.util.List;

public interface CardContextDao extends BaseMapper {
    /**
     * 获得一张卡片，随机获取
     * @return 单张卡片信息
     */
    CardContext oneCard();

    /**
     * 获取所有的卡片
     * @return 所有的卡片
     */
    List<CardContext> allCard();

}
