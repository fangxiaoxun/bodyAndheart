package com.example.team_project.dao.impl;

import com.example.team_project.dao.MailReplyDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.MailReply;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Bean("mailReply")
public class MailReplyDaoImpl extends AbsBaseMapper implements MailReplyDao {
    private static final Properties SQL;
    private static final Logger logger = Logger.getLogger(MailReplyDaoImpl.class);

    static {
        SQL = new Properties();
        try {
            SQL.load(MailReplyDaoImpl.class.getClassLoader().getResourceAsStream("sql/mail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String replyMail(long mid, long replier, String content) throws SQLException {
        MailReply reply = new MailReply();
        reply.setMid(mid);
        reply.setReplier(replier);
        reply.setContent(content);
        //获取唯一标识符，并且把'-'替换成空字符串
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        logger.debug("生成的uuid为:"+uuid);
        reply.setUuid(uuid);
        return insert(reply) > 0 ? uuid : null;
    }

    @Override
    public int delReply(String uuid) throws SQLException {
        MailReply reply = new MailReply();
        reply.setUuid(uuid);
        return delete(reply);
    }

    @Override
    public int setRead(String uuid) throws SQLException {
        String sql = SQL.getProperty("setRead");
        return jdbcUtils.update(sql,uuid);
    }

    @Override
    public int setUnread(String uuid) throws SQLException {
        String sql = SQL.getProperty("setUnRead");
        return jdbcUtils.update(sql,uuid);
    }

    @Override
    public int setRead(List<String> uuid) throws SQLException {
        String sql = SQL.getProperty("setRepliesReadPath");
        return jdbcUtils.update(sql, uuid.toArray());
    }

    @Override
    public List<MailReply> getReplies(long uid) {
        String sql = SQL.getProperty("getReplies");
        List<MailReply> query = jdbcUtils.query(sql, MailReply.class, uid);
        logger.debug("id为:"+ uid + "的用户获取到了" + query.size() + "条记录。");
        return query;
    }

    @Override
    public List<MailReply> getRepliesByPage(long uid, int page, int size) {
        String sql = SQL.getProperty("getRepliesByPage");
        List<MailReply> query = jdbcUtils.query(sql, MailReply.class, uid, (page - 1), size);
        logger.debug("id为:"+ uid + "的用户获取到了" + query.size() + "条记录。");
        return query;
    }

    @Override
    public List<MailReply> getAllReply(long mid) {
        MailReply reply = new MailReply();
        reply.setMid(mid);
        return select(reply);
    }

    @Override
    public long replyCount(long mid) {
        MailReply reply = new MailReply();
        reply.setMid(mid);
        return count(reply);
    }

    @Override
    public MailReply getOneReply(long mid) {
        MailReply reply = new MailReply();
        reply.setMid(mid);
        reply.setRead(false);
        return this.selectOne(reply);
    }


    @Override
    public String getTableName() {
        return "mail_reply";
    }
}
