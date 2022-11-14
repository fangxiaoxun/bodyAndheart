package com.example.team_project.pojo;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.example.team_project.service.IconService;
import com.example.team_project.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 运动模块对于问题的回答的实体
 */
public class SportAnswer {
    /**
     * 回答的唯一标识
     */
    private String uuid;
    /**
     * 回答的问题的标识
     */
    private String question;
    /**
     * 回答的内容
     */
    private String answer;
    /**
     * 回答者
     * 下面没有get方法，但是有一个get方法获取replier的详细信息
     */
    private Long replier;
    /**
     * 点赞人数,通过多表查询获取
     */
    @Param(value = "",ignore = true)
    private Long likeCount;
    /**
     * 回答日期
     */
    @Param("reply_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime replyTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setReplier(Long replier) {
        this.replier = replier;
    }

    public LocalDateTime getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(LocalDateTime replyTime) {
        this.replyTime = replyTime;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public User getReplier() {
        User replier = null;
        if (this.replier != null) {
            UserService userService = BeanFactory.getBean("userService", UserService.class);
            replier = userService.getUserInfo(this.replier);
            IconService iconService = BeanFactory.getBean("iconService", IconService.class);
            replier.setIconUrl(iconService.getUserIconByMark(replier.getIconMark()).getIconUrl());
        }
        return replier;
    }

    @Override
    public String toString() {
        return "SportAnswer{" +
                "uuid='" + uuid + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", replier=" + replier +
                ", replyTime=" + replyTime +
                '}';
    }
}
