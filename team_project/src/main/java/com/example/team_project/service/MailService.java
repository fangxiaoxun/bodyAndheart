package com.example.team_project.service;

import com.example.team_project.pojo.Mail;
import com.example.team_project.pojo.MailReply;

import java.util.List;
import java.util.Map;

public interface MailService {
    /**
     * 随机获取岛
     * @return 获取指定数量的信
     */
    List<Mail> randomMails(int count, Long uid);

    /**
     * 发邮件到公共岛
     * @param wuid 发邮件的用户id
     * @param body 邮件的具体内容
     * @return 是否发布成功
     */
    boolean writeMail(long wuid, String body);

    /**
     * 回复公共岛邮件
     * @param replyUid 发送回复的用户的id
     * @param body 回复的正文
     * @return 如果回复成功则返回回复的唯一标识，若失败则返回null
     */
    String replyMail(long mid, long replyUid,String body);

    /**
     * 设置为未读
     * @param uuid 回复的唯一标识
     * @return 是否成功
     */
    boolean setRead(String uuid);

    /**
     * 设置为已读，期望的写法为，发信人阅读了回复，使用这个
     * @param uuid 回复的唯一标识
     * @return 是否成功
     */
    boolean setUnRead(String uuid);

    /**
     * 获取单篇信，并且返回的是有未读评论的信
     * @param uid 查询的用户的id
     * @return 单篇信，附带评论
     */
    Mail getMailWithReply(long uid);

    /**
     * 历史信件（含回复）
     * @param uid 查看历史信件的用户
     * @return 历史信件（含回复）
     */
    List<Mail> historyMails(long uid);

    /**
     * 历史信件（分页）,含信的总数
     * @return 历史信件（不含回复，但分页）
     */
    Map<String, Object> historyMails(long uid, int currentPage, int size);

    /**
     * 一封信的历史回复
     * @param mid 信的id
     * @param rCurrentPage 当前页码
     * @param rSize 一页展示的数量
     * @return 包括信的内容，回复和回复总数
     */
    Map<String, Object> historyMail(long mid, int rCurrentPage, int rSize);

    /**
     * 获得单封信的回复
     * @param mid 信的id
     * @return 这封信的所有回复
     */
    Mail historyMail(long mid);
}
