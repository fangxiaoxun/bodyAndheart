package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

import java.io.Serializable;
import java.util.Objects;

/**
 *  实体类question
 */
public class Question implements Serializable {
    @Param("user")
    private Long userId;
    @Param("question")
    private Integer question;
    @Param("describe")
    private String describe;
    @Param("answer")
    private String answer;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return question.equals(question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }

    @Override
    public String toString() {
        return "Question{" +
                "userId=" + userId +
                ", question=" + question +
                ", describe='" + describe + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
