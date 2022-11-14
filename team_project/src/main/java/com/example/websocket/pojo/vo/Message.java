package com.example.websocket.pojo.vo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.example.team_project.pojo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message {
    @Param(value = "isSystem",ignore = true)
    private boolean isSystem;
    /**
     * 在聊天记录中，用于通过这个属性来获取发送者
     */
    @JsonIgnore
    private Long fromWhom;
    @Param(value = "sender",ignore = true)
    private User sender;
    private String roomNum;
    private String body;
    @Param(value = "record",ignore = true)
    private Boolean isRecord;

    public Message() {
    }

    public Message(boolean isSystem, User fromWhom, String roomNum, String body) {
        this.isSystem = isSystem;
        this.sender = fromWhom;
        this.roomNum = roomNum;
        this.body = body;
    }


    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public Long getFromWhom() {
        return fromWhom;
    }

    public void setFromWhom(Long fromWhom) {
        this.fromWhom = fromWhom;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Boolean getRecord() {
        if (isRecord == null) {
            return false;
        }
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    @Override
    public String toString() {
        return "Message{" +
                "isSystem=" + isSystem +
                ", fromWhom=" + fromWhom +
                ", roomNum='" + roomNum + '\'' +
                ", body='" + body + '\'' +
                ", isRecord='" + isRecord + '\'' +
                '}';
    }
}
