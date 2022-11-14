package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 评论实体
 */
public class Comment {
    @Param("comment_id")
    private Long id;
    @Param("post_id")
    private Long postId;
    @Param("discussant")
    private Long discussant;
    @Param("body")
    private String body;
    /**
     * 需要使用聚合函数计算，点赞数
     * 功能不需要，也还没写，以防万一，留着，但进行忽略
     */
    @Param(value = "like",ignore = true)
    @JsonIgnore
    private Integer like;
    @Param("comm_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime commentTime;
    /**
     * 父评论id
     */
    @Param("father")
    @JsonIgnore
    private Long fatherId;

    /**
     * 父评论属性,无法通过数据库查
     */
    @Param(value = "fatherComment",ignore = true)
    private Comment fatherComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getDiscussant() {
        return discussant;
    }

    public void setDiscussant(Long discussant) {
        this.discussant = discussant;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public Comment getFatherComment() {
        return fatherComment;
    }

    public void setFatherComment(Comment fatherComment) {
        this.fatherComment = fatherComment;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDateTime commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", discussant=" + discussant +
                ", body='" + body + '\'' +
                ", like=" + like +
                ", commentTime=" + commentTime +
                ", fatherId=" + fatherId +
                '}';
    }
}
