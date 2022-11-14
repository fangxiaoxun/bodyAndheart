package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 邮件回复实体
 */
public class MailReply {
    private Long mid;
    private Long replier;
    private String content;
    private String uuid;
    @JsonIgnore
    private Boolean read;
    @Param("reply_time")
    @JsonFormat(pattern = "yyyy年MM月dd日",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;

    public MailReply() {
    }

    public MailReply(Long mid, Long replier, String content, String uuid) {
        this.mid = mid;
        this.replier = replier;
        this.content = content;
        this.uuid = uuid;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getReplier() {
        return replier;
    }

    public void setReplier(Long replier) {
        this.replier = replier;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MailReply{" +
                "mid=" + mid +
                ", replier=" + replier +
                ", content='" + content + '\'' +
                ", uuid='" + uuid + '\'' +
                ", read=" + read +
                '}';
    }
}

