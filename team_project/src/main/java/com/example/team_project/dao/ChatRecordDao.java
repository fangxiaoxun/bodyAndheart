package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.websocket.pojo.vo.Message;

import java.util.List;

public interface ChatRecordDao extends BaseMapper {
    /**
     * 添加聊天记录
     * @param message 聊天消息
     * @return 影响的行数
     */
    int addRecord(Message message);

    /**
     * 删除一个房间的聊天记录
     * @param roomNum 要删除的房间的id
     * @return 影响的条数
     */
    int removeRecord(String roomNum);

    /**
     * 获取一个房间的聊天记录
     * @param roomNum 房间号
     * @return 聊天记录
     */
    List<Message> getRecord(String roomNum);

    List<Message> getRecord(String roomNum, int size);

}
