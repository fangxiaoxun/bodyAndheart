package com.example.team_project.service.impl;

import com.example.team_project.dao.CardContextDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.CardContext;
import com.example.team_project.service.CardContextService;

import java.util.List;

@Bean("cardContextService")
public class CardContextServiceImpl implements CardContextService {
    @AutoWire
    private CardContextDao cardContextDao;

    @Override
    public CardContext getOneCard() {
        return cardContextDao.oneCard();
    }

    @Override
    public List<CardContext> allCard() {
        return cardContextDao.allCard();
    }
}
