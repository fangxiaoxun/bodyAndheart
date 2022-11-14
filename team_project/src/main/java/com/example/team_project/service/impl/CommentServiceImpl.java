package com.example.team_project.service.impl;

import com.example.team_project.dao.CommentDao;
import com.example.team_project.dao.impl.CommentDaoImpl;
import com.example.team_project.pojo.Comment;
import com.example.team_project.service.CommentService;

import java.sql.SQLException;
import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final CommentDao COMMENT_DAO = new CommentDaoImpl();

    @Override
    public List<Comment> getComments(long postId) {
        return COMMENT_DAO.getComments(postId);
    }

    @Override
    public List<Comment> getCommentsByPage(long postId, int currentPage, int count) {
        return COMMENT_DAO.getCommentsByPage(postId,currentPage - 1, count);
    }

    @Override
    public boolean release(Comment comment) {
        boolean result = false;
        try {
            int affect = COMMENT_DAO.releaseComment(comment);
            if (affect != 0){
                result = true;
            }
        } catch (SQLException e) {
            COMMENT_DAO.rollback();
            e.printStackTrace();
        }finally {
            COMMENT_DAO.commit();
        }
        return result;
    }

    @Override
    public long commentCount(long postId) {
        return COMMENT_DAO.commentCount(postId);
    }
}
