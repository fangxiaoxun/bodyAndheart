package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;


public class Post {
    @Param("id")
    private Long id;
    /**
     * 发布的文章属于哪个岛
     */
    @Param("belong")
    private Long belong;
    @Param("title")
    private String title;
    @Param("body")
    private String body;
    /**
     * 公开还是私密（公开为1，私密为0）
     */
    @Param("type")
    private Integer type;
    @JsonIgnore
    @Param("condition")
    private Integer conditionNum;
    /**
     * 返回给前端的动态情绪信息
     */
    @Param(value = "post_condition",ignore = true)
    private PostCondition condition;
    @Param("can_comment")
    private Boolean canComment;
    @Param("read")
    private Integer read;
    /**
     * replyCount 回复数量，回复数量用sql语句查询获取即可
     */
    @Param(value = "replyCount",ignore = true)
    private Long replyCount;
    /**
     * 帖子发布者
     */
    @Param("publisher")
    private Long publisher;
    @Param("pub_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime pubTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBelong() {
        return belong;
    }

    public void setBelong(Long belong) {
        this.belong = belong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public Long getPublisher() {
        return publisher;
    }

    public void setPublisher(Long publisher) {
        this.publisher = publisher;
    }

    public PostCondition getCondition() {
        return condition;
    }

    public void setCondition(PostCondition condition) {
        this.condition = condition;
    }

    public LocalDateTime getPubTime() {
        return pubTime;
    }

    public void setPubTime(LocalDateTime pubTime) {
        this.pubTime = pubTime;
    }

    public Long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Long replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getConditionNum() {
        return conditionNum;
    }

    public void setConditionNum(Integer condition) {
        this.conditionNum = condition;
    }

    public Boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(Boolean canComment) {
        this.canComment = canComment;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", belong=" + belong +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", read=" + read +
                ", replyCount=" + replyCount +
                ", publisher=" + publisher +
                ", pubTime=" + pubTime +
                '}';
    }


}
