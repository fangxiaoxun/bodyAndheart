package com.example.team_project.dao.impl;

import com.example.team_project.dao.ChatRecordDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.websocket.pojo.vo.Message;

import java.sql.SQLException;
import java.util.List;

@Bean("chatRecordDao")
public class ChatRecordDaoImpl extends AbsBaseMapper implements ChatRecordDao {
    @Override
    public int addRecord(Message message) {
        int insert = 0;
        try {
            insert = insert(message);
            this.commit();
        } catch (SQLException e) {
            this.rollback();
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    public int removeRecord(String roomNum) {
        Message message = new Message();
        message.setRoomNum(roomNum);
        int delete = 0;
        try {
            delete = delete(message);
            commit();
        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
        }
        return delete;
    }

    @Override
    public List<Message> getRecord(String roomNum) {
        Message message = new Message();
        message.setRoomNum(roomNum);
        return select(message);
    }

    @Override
    public List<Message> getRecord(String roomNum, int size) {
        String base = "select *from chat_record order by desc limit 0,?";
        return jdbcUtils.query(base, Message.class, size);
    }

    @Override
    public String getTableName() {
        return "chat_record";
    }
}
