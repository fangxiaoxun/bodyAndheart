package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

/**
 * 兴趣标签
 */
public class Hobby {
    @Param("hobby")
    private Integer hobby;
    @Param("name")
    private String name;


    public int getHobby() {
        return hobby;
    }

    public void setHobby(int hobby) {
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "hobby=" + hobby +
                ", name='" + name + '\'' +
                '}';
    }
}
