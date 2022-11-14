package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Mail;

import java.sql.SQLException;
import java.util.List;


public interface MailDao extends BaseMapper {

//    /**
//     * 返回一定数量的邮件(可能不适合写在Dao层，个人觉得应该是放service层)
//     * @param count 一次获取的数量
//     * @return 包含着获取数量的邮件
//     */
//    List<Mail> randomMail(int count);

    /**
     * 写信
     * @param mail 信的内容
     * @return 影响的条目数
     */
    int writeMail(Mail mail) throws SQLException;

    /**
     * 获取信的数量（也可以为最大的id）
     * @return 数量（也为最大的id）
     */
    long getCount();

    /**
     * 查看某个用户发送的条目数
     * @param uid 要查看的用户
     * @return 该用户发送的条目数
     */
    long sendCount(long uid);

    /**
     * 获取单个邮件信息，可以用来判断是否存在某邮件
     * @param mid 邮件的唯一标识
     * @return 如果uuid有对应的邮件，则返回邮件，若无，则返回null
     */
    Mail getOneMail(long mid);

    /**
     * 把信设置为已读（能够减少信的推送）
     * @param mid 设为已读的信的id
     * @param uid 设为已读的用户
     * @return 影响的条数
     */
    int mailRead(long mid, long uid) throws SQLException;

    /**
     * 是否已读
     * @param mid 要查看是否已读的信的id
     * @param uid 要查看是否已读的用户的id
     * @return 已读为true，未读为false
     */
    boolean hasRead(long mid, long uid);

    /**
     * 获取一定数量的信，根据传入的mid数量决定
     * @param mids 包含信id的数组
     * @return 具有一定数量信的集合
     */
    List<Mail> getMails(Long[] mids);

    /**
     * 查询用户读过的信数量
     * @param uid 要进行查询的用户
     * @return 返回数量
     */
    long noNeedReturnCount(long uid);

    /**
     * 查看某用户发过的信
     * @param uid 想要查看的用户的uid
     * @return 该用户发过的信的数据
     */
    List<Mail> getOnesSends(long uid);

    List<Mail> getOnesSends(long uid, int currentPage, int size);

    /**
     * 查看某个用户曾经发过的信的数量
     * @param uid 想要查看的用户的uid
     * @return 曾经发过的信的数量
     */
    long getOnesSendCount(long uid);

    /**
     * 获取单封带评论的信，有未读评论的
     * @param uid 要获取信的用户uid
     */
    Mail getOneNoReply(long uid);


}
