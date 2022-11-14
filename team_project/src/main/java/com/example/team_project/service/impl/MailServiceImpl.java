package com.example.team_project.service.impl;

import com.example.team_project.dao.MailDao;
import com.example.team_project.dao.MailReplyDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.Mail;
import com.example.team_project.pojo.MailReply;
import com.example.team_project.service.MailService;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

@Bean("mailService")
public class MailServiceImpl implements MailService {
    @AutoWire
    private MailDao mailDao;
    @AutoWire
    private MailReplyDao mailReplyDao;
    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class);

    @Override
    public List<Mail> randomMails(int count, Long uid) {
        //使得传入数据没有问题
        if (count > 0 && uid != null) {
            //set集合不重复，且hashSet不排序
            Set<Long> mids = new HashSet<>(count);
            //获取最大的信的id
            long maxId = mailDao.getCount();
            //获取不需要获取的信数量
            long noNeedReturnCount = mailDao.noNeedReturnCount(uid);
            if (maxId != 0) {
                //保证剩下的信足够返回count条
                if (maxId - noNeedReturnCount > count) {
                    //使得返回数量是传入的参数
                    while (mids.size() < count) {
                        long mid = (long) (Math.random() * maxId + 1);
                        Mail mail = mailDao.getOneMail(mid);
                        //信是自己的，则进行下一次循环
                        if (mail.getSender().equals(uid)) {
                            continue;
                        }
                        //判断这封信是否是已读
                        boolean hasRead = mailDao.hasRead(mid, uid);
                        //已读的不返回
                        if (!hasRead) {
                            mids.add(mid);
                        }
                    }
                } else if (maxId - noNeedReturnCount > 0) {
                    int needCount = (int) (maxId - noNeedReturnCount);
                    //去除已读
                    for (int i = 1; i <= needCount; i++) {
                        if (!mailDao.hasRead(i, uid)) {
                            Mail mail = mailDao.getOneMail(i);
                            //去除发信人是自己的情况
                            if (!mail.getSender().equals(uid)) {
                                mids.add((long) i);
                            }
                        }
                    }
                }
                return mailDao.getMails(mids.toArray(new Long[0]));
            }
        } else if (uid == null && count > 0) {
            //若为游客，则随机获取
            Set<Long> mids = new HashSet<>(count);
            long maxId = mailDao.getCount();
            if (maxId < count) {
                for (long i = 1; i <= maxId; i++) {
                    mids.add(i);
                }
            }else {
                while (mids.size() < count) {
                    long index = (long) (Math.random() * maxId + 1);
                    mids.add(index);
                }
            }
            return mailDao.getMails(mids.toArray(new Long[0]));
        }
        //当一封需要获取信都没有的时候，才会返回什么都没有的数组
        return new ArrayList<>();
    }

    @Override
    public boolean writeMail(long wuid, String body) {
        boolean result = false;
        if (body != null) {
            LOGGER.debug("用户:" + wuid + " 发出了邮件");
            Mail mail = new Mail();
            mail.setSender(wuid);
            mail.setBody(body);
            try {
                int affect = mailDao.writeMail(mail);
                if (affect != 0) {
                    LOGGER.info("用户:" + wuid + " 发邮件成功");
                    result = true;
                }
            } catch (SQLException e) {
                mailDao.rollback();
                e.printStackTrace();
            } finally {
                mailDao.commit();
            }
        }
        return result;
    }

    @Override
    public String replyMail(long mid, long replyUid, String body) {
        String result = null;
        if (mid > 0 && replyUid > 0 && body != null) {
            try {
                String uuid = mailReplyDao.replyMail(mid, replyUid, body);
                if (uuid != null) {
                    LOGGER.info("用户:" + replyUid + " 回复了id为" + mid + "的信");
                    result = uuid;
                }
            } catch (SQLException e) {
                mailReplyDao.rollback();
                e.printStackTrace();
            } finally {
                mailReplyDao.commit();
            }
        }
        return result;
    }

    @Override
    public boolean setRead(String uuid) {
        boolean result = false;
        if (uuid != null) {
            try {
                int affect = mailReplyDao.setRead(uuid);
                if (affect != 0) {
                    result = true;
                }
            } catch (SQLException e) {
                mailReplyDao.rollback();
                e.printStackTrace();
            } finally {
                mailReplyDao.commit();
            }
        }
        return result;
    }

    @Override
    public boolean setUnRead(String uuid) {
        boolean result = false;
        if (uuid != null) {
            try {
                int affect = mailReplyDao.setUnread(uuid);
                if (affect != 0) {
                    result = true;
                }
            } catch (SQLException e) {
                mailReplyDao.rollback();
                e.printStackTrace();
            } finally {
                mailReplyDao.commit();
            }
        }
        return result;
    }

    @Override
    public Mail getMailWithReply(long uid) {
        Mail mail = null;
        if (uid > 0) {
            mail = mailDao.getOneNoReply(uid);
            if (mail != null) {
                MailReply reply = mailReplyDao.getOneReply(mail.getMid());
                mail.setReplies(reply);
                LOGGER.debug("获取到" + mail);
                try {
                    int affect = mailReplyDao.setRead(reply.getUuid());
                    if (affect > 0) {
                        LOGGER.debug("回复被设置为已读!");
                    } else {
                        LOGGER.debug("回复" + reply + "设置已读失败");
                    }
                } catch (SQLException e) {
                    mailReplyDao.rollback();
                    e.printStackTrace();
                } finally {
                    mailReplyDao.commit();
                }
            }
        }
        return mail;
    }

    @Override
    public List<Mail> historyMails(long uid) {
        if (uid > 0) {
            //获取历史信件
            List<Mail> onesSends = mailDao.getOnesSends(uid);
            LOGGER.debug(uid + "用户的历史信件数为:" + onesSends.size());
            LOGGER.debug("Start set replies...");
            //设置历史信件的回复
            for (Mail mail : onesSends) {
                Long mid = mail.getMid();
                List<MailReply> allReply = mailReplyDao.getAllReply(mid);
                LOGGER.debug(mid + "的回复数量为:" + allReply.size());
                mail.setReplies(allReply);
            }
            LOGGER.debug("End of setting replies...");
            return onesSends;
        }else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Map<String, Object> historyMails(long uid, int currentPage, int size) {
        if (uid > 0 && currentPage > 0 && size > 0) {
            HashMap<String, Object> map = new HashMap<>();
            //获取数量
            long count = mailDao.getOnesSendCount(uid);
            map.put("count", count);
            //分页获取发送的内容
            List<Mail> onesSends = mailDao.getOnesSends(uid, currentPage - 1, size);
            map.put("mails",onesSends);
            return map;
        }
        return null;
    }

    @Override
    public Map<String, Object> historyMail(long mid, int rCurrentPage, int rSize) {
        Map<String,Object> map = new HashMap<>();
        if (mid > 0 && rCurrentPage > 0 && rSize > 0) {
            //获取单封信
            Mail mail = mailDao.getOneMail(mid);
            if (mail != null) {
                //分页获取回复
                MailReply reply = new MailReply();
                reply.setMid(mid);
                List<MailReply> replies = mailReplyDao.select(reply, rCurrentPage - 1, rSize);
                mail.setReplies(replies);
                map.put("mail", mail);
                LOGGER.debug(mid + "信件 获取回复"+replies.size()+"条");
                //获取回复数
                map.put("replyCount", mailReplyDao.replyCount(mid));
            }
        }
        return map;
    }

    @Override
    public Mail historyMail(long mid) {
        if (mid > 0) {
            Mail mail = mailDao.getOneMail(mid);
            mail.setReplies(mailReplyDao.getAllReply(mid));
            return mail;
        }
        return null;
    }


}
