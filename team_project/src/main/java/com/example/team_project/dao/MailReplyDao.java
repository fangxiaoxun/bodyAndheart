package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Mail;
import com.example.team_project.pojo.MailReply;

import java.sql.SQLException;
import java.util.List;

public interface MailReplyDao extends BaseMapper {

    /**
     * 回复邮件
     * @param mid 回复的邮件
     * @param replier 回复人
     * @param content 回复的内容
     * @return 返回回复的唯一标识，若回复失败，则返回null
     */
    String replyMail(long mid, long replier, String content) throws SQLException;

    /**
     * 删除回复信息
     * @param uuid 回复的唯一标识
     * @return 影响的条数
     */
    int delReply(String uuid) throws SQLException;

    /**
     * 设置为已读
     * @param uuid 回复的唯一标识
     * @return 影响的条数
     */
    int setRead(String uuid) throws SQLException;

    /**
     * 设置为未读
     * @param uuid 邮件回复的唯一标识
     * @return 影响的条目数
     */
    int setUnread(String uuid) throws SQLException;

    /**
     * 批量给回复设置为已读
     * @param uuid 回复的唯一标志的数组
     * @return 影响的条数
     * @throws SQLException
     */
    int setRead(List<String> uuid) throws SQLException;

    /**
     * 获取所有的邮件回复,去除已读
     * @param uid 发信人id
     * @return 返回查询的信的回复，若为0条，则返回长度为0的集合
     */
    List<MailReply> getReplies(long uid);

    /**
     * 获取所有的邮件回复,去除已读,且分页
     * @param mid 要获取的回复属于回复哪封信
     * @param page 目前到了第几页
     * @param size 一次返回的最多条数
     * @return 返回查询的信的回复，若为0条，则返回长度为0的集合，若不为0条，最大值为size条
     */
    List<MailReply> getRepliesByPage(long mid, int page, int size);

    /**
     * 获取某封信的所有回复
     * @param mid 要获取回复的信
     * @return 这封信的所有回复
     */
    List<MailReply> getAllReply(long mid);

    /**
     * 获取某封信的回复数量
     * @param mid 要获取回复数的信
     * @return 回复数量
     */
    long replyCount(long mid);

    /**
     * 通过信，获取一条未读的回复(该信中的)
     * @param mid 信的id
     * @return 一条未读的回复
     */
    MailReply getOneReply(long mid);
}
