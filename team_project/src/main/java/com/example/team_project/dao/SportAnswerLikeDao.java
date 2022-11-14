package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.SportAnswerLike;

import java.util.List;
import java.util.Set;

public interface SportAnswerLikeDao extends BaseMapper {
    /**
     * 获取这个回答的所有点赞信息
     * @param answerId 要查看的回答
     * @return 一个回答的所有的点赞信息
     */
    Set<SportAnswerLike> getLikeDetail(String answerId);

    /**
     * 查询某个回答的点赞数
     * @param answerId 要查询的回答
     * @return 点赞数
     */
    long likeCount(String answerId);

    /**
     * 点赞
     * @return 点赞是否成功
     */
    boolean like(String answerId, long uid);

    /**
     * 删除点赞
     * @return 点赞是否成功
     */
    boolean removeLike(String answerId, long uid);

    /**
     * 获取点赞信息
     * @param answerId 查看这个id是否被这个用户点过赞
     * @param uid 要查看点赞信息的用户
     * @return 若为null则为没有点赞过，若不为null，则点赞过
     */
    SportAnswerLike alreadyLike(String answerId, long uid);




}
