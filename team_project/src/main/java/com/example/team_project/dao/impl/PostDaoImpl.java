package com.example.team_project.dao.impl;

import com.example.team_project.dao.PostDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.Post;
import com.example.team_project.pojo.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class PostDaoImpl extends AbsBaseMapper implements PostDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(PostDaoImpl.class.getClassLoader().getResourceAsStream("sql/island.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getPostByPage(long island, int current, int count, User user) {
        String sql = null;
        //如果非游客访问则判断是否为岛主，为岛主，则可以查看私密内容
        if (user != null) {
            if (user.getId().equals(island)) {
                sql = SQL.getProperty("getPostByPage");
            }
        }
        //未登录或不为岛主则只能查看公开内容
        if (sql == null) {
            sql = SQL.getProperty("getPostByPageNormal");
        }
        List<Post> posts = jdbcUtils.query(sql, Post.class, island, current, count);
        addReplyCount2List(posts);
        return posts;
    }

    @Override
    public List<Post> getPost(long island) {
        String sql = SQL.getProperty("getPost");
        List<Post> posts = jdbcUtils.query(sql, Post.class, island);
        addReplyCount2List(posts);
        return posts;
    }

    private void addReplyCount2List(List<Post> posts) {
        long[] postIds = new long[posts.size()];
        for (int i = 0; i < posts.size(); i++) {
            postIds[i] = posts.get(i).getId();
        }
        long[] counts = getPostReplyCount(postIds);
        for (int i = 0; i < posts.size(); i++) {
            posts.get(i).setReplyCount(counts[i]);
        }
    }

    @Override
    public int release(long belong, String title, String body, long publisher, int condition, int type, boolean canComment) throws SQLException {
        Post post = new Post();
        post.setBelong(belong);
        post.setTitle(title);
        post.setBody(body);
        post.setPublisher(publisher);
        post.setConditionNum(condition);
        post.setType(type);
        post.setCanComment(canComment);
        return insert(post);
    }

    @Override
    public int delete(long id, long deleter) throws SQLException {
        Post post = new Post();
        post.setId(id);
        //必须是帖子的发布者才可以删帖
        post.setPublisher(deleter);

        return delete(post);
    }

    @Override
    public Post onePost(long postId) {
        Post post = new Post();
        post.setId(postId);
        return selectOne(post);
    }

    @Override
    public int addRead(long postId) throws SQLException {
        String sql = SQL.getProperty("addRead");
        return jdbcUtils.update(sql, postId);
    }

    @Override
    public long getPostReplyCount(long postId) {
        String sql = SQL.getProperty("getPostReplyCount");
        Map<String, Object> map = jdbcUtils.queryForMap(sql, postId);
        return (long) map.get("count");
    }

    @Override
    public long[] getPostReplyCount(long... postId) {
        //若postId一个没传，则返回null
        long[] result = null;
        if (postId != null && postId.length > 0) {
            result = new long[postId.length];
            String fragment = SQL.getProperty("getPostReplyCount");
            StringBuilder sql = new StringBuilder(fragment.substring(0, fragment.lastIndexOf("WHERE") + 6));
            //添加第一个，特殊处理
            sql.append("post_id = ").append(postId[0]);
            //添加其他的
            for (int i = 1; i < postId.length; i++) {
                sql.append(" OR post_id = ").append(postId[i]);
            }
            //分组查询
            sql.append(" GROUP BY post_id ORDER BY post_id desc");
            try {
                //一个一个添加进数组
                List<Map<String, Object>> maps = jdbcUtils.queryForList(sql.toString());
                for (int i = 0; i < maps.size(); i++) {
                    result[i] = (long) maps.get(i).get("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public long getPostCount(long belong) {
        String sql = SQL.getProperty("getPostCount");
        Map<String, Object> map = jdbcUtils.queryForMap(sql, belong);
        return (long) map.get("count");
    }

    @Override
    public long getReleaseNowId(long belong, String title, long publisher) {
        String sql = SQL.getProperty("getPostLastRelease");
        Map<String, Object> query = jdbcUtils.queryForMap(sql, belong, publisher, title);
        return (long) query.get("id");
    }

    @Override
    public boolean hasRelease(long iid) {
        Post post = new Post();
        post.setBelong(iid);
        Post query = selectOne(post);
        return query != null;
    }

    @Override
    public String getTableName() {
        return "post";
    }
}
