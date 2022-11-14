package com.example.team_project.pojo;

import com.example.team_project.framkwork.jdbc.annotation.Param;

import java.util.Objects;

public class PostLike {
    @Param("post_id")
    private Long postId;
    @Param("liker")
    private Long liker;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
        if (!(o instanceof PostLike)) return false;
        PostLike postLike = (PostLike) o;
        return postId.equals(postLike.postId) && liker.equals(postLike.liker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, liker);
    }

    @Override
    public String toString() {
        return "PostLike{" +
                "postId=" + postId +
                ", liker=" + liker +
                '}';
    }

}
