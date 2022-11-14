package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.PostLike;

public interface PostLikeDao extends BaseMapper {
    /**
     * 判断用户是否为该帖子（一个）点赞
     * @param postId 帖子的id,不为null
     * @param uid 判断的用户的id，一般判断当前登录
     * @return 已经点赞返回true，未点赞返回false
     */
    boolean alreadyLike(long postId, long uid);

    /**
     * 判断用户是否为这些帖子(一个或多个)点赞
     * @param uid 判断的用户的id，一般判断当前登录
     * @param postId 帖子的id，传入要从id大到小,不为null
     * @return 已经点赞的帖子返回true，未点赞的帖子返回false
     */
    boolean[] alreadyLikes(long uid, long... postId);

    /**
     * 点赞
     * @param postId 点赞的帖子
     * @return 是否点赞成功
     */
    boolean like(long uid,long postId);

    /**
     * 取消点赞
     * @param postId 删除的评论在哪个帖子
     * @param uid 删除的评论是哪个用户的
     * @return 是否删除成功
     */
    boolean delete(Long postId, Long uid);

    /**
     * 获取点赞的数量
     * @param pid 帖子的id
     * @return 点赞数量
     */
    long likeCount(long pid);
}
