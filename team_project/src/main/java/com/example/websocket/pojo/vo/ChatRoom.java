package com.example.websocket.pojo.vo;

import com.example.websocket.content.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 聊天室的相关属性
 */
public class ChatRoom {
    /**
     * 聊天室房间号
     */
    private String roomNum;
    @JsonIgnore
    private RoomType type;
    /**
     * 聊天室房间讨论主题的描述
     */
    private String desc;
    /**
     * 聊天室创建的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    public ChatRoom(String roomNum, String desc, RoomType type) {
        this.roomNum = roomNum;
        this.desc = desc;
        this.type = type;
        createTime = LocalDateTime.now();
    }

    public ChatRoom(String roomNum, RoomType type) {
        this.roomNum = roomNum;
        this.type = type;
        createTime = LocalDateTime.now();
    }

    private ChatRoom() {
    }

    /**
     * 用于获取以ChatRoom为键的map的值，这个ChatRoom不会给时间赋值
     * @return 没有时间和desc，只有roomNum的ChatRoom
     */
    public static ChatRoom createCondition(String roomNum) {
        ChatRoom room = new ChatRoom();
        room.roomNum = roomNum;
        return room;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public RoomType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomNum='" + roomNum + '\'' +
                ", desc='" + desc + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return roomNum.equals(chatRoom.roomNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNum);
    }
}
