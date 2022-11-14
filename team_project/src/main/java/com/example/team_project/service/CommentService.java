package com.example.team_project.service;

import com.example.team_project.pojo.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 获取id对应的动态的评论
     * @param postId 动态的id，评论属于的动态
     * @return 动态的评论，非null
     */
    List<Comment> getComments(long postId);

    /**
     * 同getComments，只不过为分页操作
     * @param currentPage 当前的页数
     * @param count 一页展示数目
     * @return 动态的评论，非null
     */
    List<Comment> getCommentsByPage(long postId, int currentPage, int count);

    /**
     * 发表评论
     * @param comment 评论的属性
     * @return 评论失败或成功
     */
    boolean release(Comment comment);

    /**
     * 帖子评论的数量
     * @param postId 帖子的回复数量
     * @return 帖子回复数
     */
    long commentCount(long postId);

}
