package com.example.team_project.service.impl;

import com.example.team_project.dao.ChatRecordDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.service.ChatRecordService;
import com.example.team_project.service.UserService;
import com.example.websocket.pojo.vo.Message;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

@Bean("chatRecordService")
public class ChatRecordServiceImpl implements ChatRecordService {
    @AutoWire
    private ChatRecordDao recordDao;
    @AutoWire
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(ChatRecordServiceImpl.class);

    @Override
    public boolean addRecord(Message message) {
        boolean flag = false;
        //聊天记录添加进数据库
        int affect = recordDao.addRecord(message);
        if (affect > 0) {
            flag = true;
        }
        LOGGER.debug("add chat record --> size = " + affect);
        return false;
    }

    @Override
    public boolean delRecords(String roomNum) {
        boolean flag = false;
        //删除单个房间内的聊天记录
        int affect = recordDao.removeRecord(roomNum);
        if (affect > 0) {
            flag = true;
        }
        LOGGER.debug("root:" + roomNum + " remove chat record ==> size = " + affect);
        return flag;
    }

    @Override
    public List<Message> getRecords(String roomNum, int size) {
        if (roomNum != null && size >= 0) {
            List<Message> record = recordDao.getRecord(roomNum, size);
            //把所有的聊天记录设置为true
            for (Message message : record) {
                message.setRecord(true);
                //设置发消息的用户的信息
                message.setSender(userService.getUserInfo(message.getFromWhom()));
            }
            LOGGER.debug("期望获得" + size + "条聊天记录，实际获取到了" + record.size() + "条");
            return record;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Message> getRecords(String roomNum) {
        if (roomNum != null) {
            List<Message> record = recordDao.getRecord(roomNum);
            //把所有的聊天记录设置为true
            for (Message message : record) {
                message.setRecord(true);
                //设置发消息的用户的信息
                message.setSender(userService.getUserInfo(message.getFromWhom()));
            }
            LOGGER.debug("取出了" + record.size() + "条聊天记录");
            return record;
        }
        return new ArrayList<>(0);
    }
}
