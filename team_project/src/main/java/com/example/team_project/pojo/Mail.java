package com.example.team_project.pojo;


import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 公共岛中信的实体类
 */
public class Mail {
    private Long mid;
    private Long sender;
    private String body;
    @JsonFormat(pattern = "yyyy年MM月dd日",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;
    /**
     * 设置为忽略(查表的时候，会自动忽略该属性)，直接接收Object的话，兼容性会更好
     */
    @Param(value = "replies",ignore = true)
    private Object replies;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getReplies() {
        return replies;
    }

    public void setReplies(Object replies) {
        this.replies = replies;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mid=" + mid +
                ", sender=" + sender +
                ", body='" + body + '\'' +
                ", replies=" + replies +
                '}';
    }
}
