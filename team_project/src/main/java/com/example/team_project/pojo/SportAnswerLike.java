package com.example.team_project.pojo;

import java.util.Objects;

public class SportAnswerLike {
    /**
     * 点赞的回复
     */
    private String answer;
    /**
     * 点赞者
     */
    private Long liker;

    public SportAnswerLike() {
    }

    public SportAnswerLike(String answer, Long liker) {
        this.answer = answer;
        this.liker = liker;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getLiker() {
        return liker;
    }

    public void setLiker(Long liker) {
        this.liker = liker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SportAnswerLike)) return false;
        SportAnswerLike that = (SportAnswerLike) o;
        return answer.equals(that.answer) && liker.equals(that.liker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, liker);
    }

    @Override
    public String toString() {
        return "SportAnswerLike{" +
                "answer='" + answer + '\'' +
                ", liker=" + liker +
                '}';
    }
}
