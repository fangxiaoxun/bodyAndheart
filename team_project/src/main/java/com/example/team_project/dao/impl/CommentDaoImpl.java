package com.example.team_project.dao.impl;

import com.example.team_project.dao.CommentDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Comment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CommentDaoImpl extends AbsBaseMapper implements CommentDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(CommentDaoImpl.class.getClassLoader().getResourceAsStream("sql/comment.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comment> getComments(long postId) {
        Comment condition = new Comment();
        condition.setPostId(postId);
        List<Comment> select = select(condition);
        //逆序
        select.sort(((o1, o2) -> (int) (o2.getId() - o1.getId())));
        setFatherComment(condition,select);
        return select;
    }

    @Override
    public List<Comment> getCommentsByPage(long postId, int current, int count) {
        String sql = SQL.getProperty("getByPage");
        //获得分页的评论
        List<Comment> comments = jdbcUtils.query(sql, Comment.class, postId, current, count);
        Comment condition = new Comment();
        condition.setPostId(postId);
        //设置父评论属性
        setFatherComment(condition,comments);
        return comments;
    }

    /**
     * 设置父评论属性
     * @param condition 条件实体
     * @param comments 存放所有父评论的集合
     */
    private void setFatherComment(Comment condition, List<Comment> comments) {
        for (Comment comment : comments) {
            Long fatherId = comment.getFatherId();
            //没有父评论的评论，不用添加父评论信息
            if (fatherId != null) {
                condition.setFatherId(fatherId);
                comment.setFatherComment(selectOne(condition));
            }
        }
    }

    @Override
    public int releaseComment(Comment comment) throws SQLException {
        return insert(comment);
    }

    @Override
    public long commentCount(long postId) {
        String sql = SQL.getProperty("getCount");
        Map<String, Object> param = jdbcUtils.queryForMap(sql, postId);
        return (long) param.get("count");
    }

    /**
     * 删除帖子中的所有评论
     * @param pid
     * @return 删除的条目数
     */
    @Override
    public int delCommentsByPid (long pid) throws SQLException {
        Comment comment = new Comment();
        comment.setPostId(pid);
        return delete(comment);
    }

    @Override
    public String getTableName() {
        return "comment";
    }
}
