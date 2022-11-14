package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

/**
 * 用户的兴趣
 */
public class Interested {
    @Param("user")
    private Long user;
    @Param("hobby")
    private Integer hobby;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public int getHobby() {
        return hobby;
    }

    public void setHobby(int hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Interested{" +
                "user=" + user +
                ", hobby=" + hobby +
                '}';
    }
}
