package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.CardContext;
import com.example.team_project.service.CardContextService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/card/*")
public class CardServlet extends BaseServlet{
    private CardContextService cardContextService = BeanFactory.getBean("cardContextService", CardContextService.class);

    @ContentType
    public void oneCard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CardContext oneCard = cardContextService.getOneCard();
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("card", oneCard));
        LOGGER.debug("get card ==> " + oneCard.getTheme());
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void allCard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CardContext> allCard = cardContextService.allCard();
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("cards", allCard));
        LOGGER.debug("get cards ==> size = " + allCard.size());
        mapper.writeValue(response.getOutputStream(), msg);
    }

}
