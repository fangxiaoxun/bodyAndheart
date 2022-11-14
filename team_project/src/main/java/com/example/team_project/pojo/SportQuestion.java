package com.example.team_project.pojo;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.jdbc.annotation.Param;
import com.example.team_project.service.IconService;
import com.example.team_project.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * 在运动模块的问题实体
 */
public class SportQuestion {
    /**
     * 关于运动提问的唯一标识
     */
    private String question;
    /**
     * 提问者
     * 下面没有对应的get方法，但是有一个get方法能获取quizzer的详细信息
     */
    private Long quizzer;
    /**
     * 问题标题
     */
    private String title;
    /**
     * 问题的正文
     */
    private String body;
    /**
     * 提问时间
     */
    @Param("qtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime qTime;

    public SportQuestion() {
    }

    public SportQuestion(String question, String title, String body, long quizzer) {
        this.question = question;
        this.title = title;
        this.body = body;
        this.quizzer = quizzer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public LocalDateTime getqTime() {
        return qTime;
    }

    public void setqTime(LocalDateTime qTime) {
        this.qTime = qTime;
    }

    public User getQuizzer() {
        User quizzer = null;
        if (this.quizzer != null) {
            UserService userService = BeanFactory.getBean("userService", UserService.class);
            quizzer = userService.getUserInfo(this.quizzer);
            IconService iconService = BeanFactory.getBean("iconService", IconService.class);
            quizzer.setIconUrl(iconService.getUserIconByMark(quizzer.getIconMark()).getIconUrl());
        }
        return quizzer;
    }

    public void setQuizzer(Long quizzer) {
        this.quizzer = quizzer;
    }

    @Override
    public String toString() {
        return "SportQuestion{" +
                "question='" + question + '\'' +
                ", quizzer=" + quizzer +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", qTime=" + qTime +
                '}';
    }
}
