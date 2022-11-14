package com.example.team_project.dao.impl;

import com.example.team_project.dao.MailDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Mail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Bean("mailDao")
public class MailDaoImpl extends AbsBaseMapper implements MailDao {
    private static final Properties SQL;

    static {
        SQL = new Properties();
        try {
            SQL.load(MailDaoImpl.class.getClassLoader().getResourceAsStream("sql/mail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int writeMail(Mail mail) throws SQLException {
        return insert(mail);
    }

    @Override
    public long getCount() {
        return count(new Mail());
    }

    @Override
    public long sendCount(long uid) {
        Mail mail = new Mail();
        mail.setSender(uid);
        return count(mail);
    }

    @Override
    public Mail getOneMail(long mid) {
        Mail mail = new Mail();
        mail.setMid(mid);
        return selectOne(mail);
    }

    @Override
    public int mailRead(long mid, long uid) throws SQLException {
        String sql = SQL.getProperty("setMailRead");
        return jdbcUtils.update(sql,mid,uid);
    }

    @Override
    public boolean hasRead(long mid, long uid) {
        String sql = SQL.getProperty("hasRead");
        Map<String, Object> map = jdbcUtils.queryForMap(sql, mid, uid);
        return map.size() != 0;
    }

    @Override
    public List<Mail> getMails(Long[] mids) {
        StringBuilder sql = new StringBuilder(SQL.getProperty("getMailPartSql"));
        if (mids.length > 0) {
            sql.append(" (?");
            for (int i = 1; i < mids.length; i++) {
                sql.append(",?");
            }
            sql.append(")");
            return jdbcUtils.query(sql.toString(),Mail.class, Arrays.stream(mids).toArray());
        }
        //若传进来的参数数量有问题，则返回null
        return null;
    }

    @Override
    public long noNeedReturnCount(long uid) {
        String sql = SQL.getProperty("sendOrReadCount");
        Object count = jdbcUtils.queryForMap(sql, uid, uid).get("count");
        //若为null，则返回0条记录
        return count == null ? 0 : (long)count;
    }

    @Override
    public Mail getOneNoReply(long uid) {
        String sql = SQL.getProperty("getOneMailIfRepliesNoRead");
        Map<String, Object> map = jdbcUtils.queryForMap(sql, uid);
        return MapUtils.instanceByMap(Mail.class, map);
    }

    @Override
    public List<Mail> getOnesSends(long uid) {
        Mail condition = new Mail();
        condition.setSender(uid);
        return select(condition);
    }

    @Override
    public List<Mail> getOnesSends(long uid, int currentPage, int size) {
        Mail mail = new Mail();
        mail.setSender(uid);
        return select(mail, currentPage, size);
    }

    @Override
    public long getOnesSendCount(long uid) {
        Mail mail = new Mail();
        mail.setSender(uid);
        return count(mail);
    }

    /**
     * 只有mail表可以使用AbsBaseMapper的方法
     */
    @Override
    public String getTableName() {
        return "mail";
    }
}
