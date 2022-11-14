package com.example.team_project.pojo;

public class IslandMark {

    private Long island;

    private Long follower;

    public IslandMark(Long island, Long follower) {
        this.island = island;
        this.follower = follower;
    }

    public IslandMark() {
    }

    public Long getIsland() {
        return island;
    }

    public void setIsland(Long island) {
        this.island = island;
    }

    public Long getFollower() {
        return follower;
    }

    public void setFollower(Long follower) {
        this.follower = follower;
    }
}
