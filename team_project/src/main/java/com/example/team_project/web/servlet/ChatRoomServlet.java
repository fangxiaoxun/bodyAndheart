package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.websocket.ChatEndpoint;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/room/*")
public class ChatRoomServlet extends BaseServlet{
    @ContentType
    public void chatRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> roomList = ChatEndpoint.chatRooms();
        mapper.writeValue(response.getOutputStream(), roomList);
    }

    @ContentType
    public void sportRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> roomList = ChatEndpoint.sportRooms();
        mapper.writeValue(response.getOutputStream(), roomList);
    }

    @ContentType
    public void checkLegal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomNum = request.getParameter("roomNum");
        //判断合法性
        boolean legitimacy = ChatEndpoint.checkRoomNumLegitimacy(roomNum);
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("legitimacy", legitimacy));
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获得一个可用的房间号
     */
    @ContentType
    public void getAvailable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int roomNum;
        do {
            roomNum = (int) (Math.random() * Integer.MAX_VALUE);
        }while (!ChatEndpoint.checkRoomNumLegitimacy(roomNum + ""));
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("roomNum", roomNum));
        mapper.writeValue(response.getOutputStream(), msg);
    }
}
