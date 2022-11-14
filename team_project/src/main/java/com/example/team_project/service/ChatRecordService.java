package com.example.team_project.service;

import com.example.websocket.pojo.vo.Message;

import java.util.List;

public interface ChatRecordService {
    boolean addRecord(Message message);

    boolean delRecords(String roomNum);

    List<Message> getRecords(String roomNum, int size);

    List<Message> getRecords(String roomNum);
}
