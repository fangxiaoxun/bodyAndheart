package com.example.team_project.service;

/**
 * 动态点赞的业务逻辑层
 */
public interface PostLikeService {
    /**
     * 是否已经对帖子点赞
     * @param uid 查询的用户id
     * @param postId 查询的帖子
     * @return 是否已经点赞
     */
    boolean alreadyLike(long uid, long postId);

    /**
     * 同上，但为多值
     */
    boolean[] alreadyLikes(long uid, long... postId);

    /**
     * 取消点赞
     * @param uid
     * @param postId
     * @return 是否成功取消
     */
    boolean delete(long uid, long postId);

    /**
     * 点赞
     * @return 是否成功点赞
     */
    boolean like(long uid, long postId);

    /**
     * 通过帖子的id来获取点赞数
     * @param pid
     * @return 点赞数
     */
    long likeCount(long pid);

}
