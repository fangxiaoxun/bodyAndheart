package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Comment;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>已经有数量了，不用再通过这个CommentDao获取</p>
 * 评论的相关数据库操作
 */
public interface CommentDao extends BaseMapper {

    /**
     * 获取评论，全部
     * @param postId 动态的id
     * @return 评论的集合，不为null
     */
    List<Comment> getComments(long postId);

    /**
     * 分页获取评论
     * @param postId 动态的id
     * @param current 当前页码
     * @param count 一次获取的数量
     * @return 评论的集合，不为null
     */
    List<Comment> getCommentsByPage(long postId, int current, int count);

    /**
     * 发布评论
     * @return 影响条数
     */
    int releaseComment(Comment comment) throws SQLException;

    /**
     * 某个帖子评论的数量
     * @param postId 帖子的id
     * @return 评论的数量
     */
    long commentCount(long postId);

    int delCommentsByPid(long pid) throws SQLException;

}
