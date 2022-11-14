package com.example.team_project.dao.impl;

import com.example.team_project.dao.SportAnswerLikeDao;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.SportAnswerLike;
import com.example.team_project.utils.ServiceConfUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例bean，运动问答的点赞
 */
@Bean("sportAnswerLikeDao")
public class SportAnswerLikeDaoImpl extends AbsBaseMapper implements SportAnswerLikeDao {
    /**
     * <p>运动点赞的缓存</p>
     * <p>key为回答的id，value为该id对应的所有点赞数据</p>
     */
    private final Map<String, Set<SportAnswerLike>> LIKE = new ConcurrentHashMap<>(CAPACITY);
    /**
     * 运动点赞改变的情况，用于持久化,true为增加的点赞，false为取消的点赞
     */
    private final Map<Boolean, Set<SportAnswerLike>> CHANGE = new ConcurrentHashMap<>(4);

    /**
     * 最后一次保存的时间
     */
    private long lastTimeSave;
    /**
     * set集合默认大小，不确定会放入多少个
     */
    private static final int CAPACITY = 16;

    /**
     * 初始化LIKE缓存
     */
    public SportAnswerLikeDaoImpl() {
        //获取所有的点赞信息
        List<SportAnswerLike> select = select(new SportAnswerLike());
        for (SportAnswerLike sportAnswerLike : select) {
            String answer = sportAnswerLike.getAnswer();
            if (LIKE.containsKey(answer)) {
                LIKE.get(answer).add(sportAnswerLike);
            }else {
                Set<SportAnswerLike> likes = new HashSet<>(CAPACITY);
                likes.add(sportAnswerLike);
                LIKE.put(answer, likes);
            }
        }
        //初始化改变记录的容器
        CHANGE.put(true, new HashSet<>(CAPACITY));
        CHANGE.put(false, new HashSet<>(CAPACITY));
        //加载时间
        lastTimeSave = System.currentTimeMillis();
    }

    @Override
    public Set<SportAnswerLike> getLikeDetail(String answerId) {
        if (LIKE.containsKey(answerId)) {
            return LIKE.get(answerId);
        }
        final int ENTRY = 0;
        //返回空集合
        return new HashSet<>(ENTRY);
    }

    @Override
    public long likeCount(String answerId) {
        long count = 0;
        if (LIKE.containsKey(answerId)) {
            count = LIKE.get(answerId).size();
        }
        return count;
    }

    @Override
    public boolean like(String answerId, long uid) {
        //先进行持久化
        cacheManage();
        boolean result;
        SportAnswerLike like = new SportAnswerLike(answerId, uid);
        //取消点赞的记录
        Set<SportAnswerLike> removeLikes = CHANGE.get(false);
        //增加点赞的记录
        Set<SportAnswerLike> addLikes = CHANGE.get(true);
        if (LIKE.containsKey(answerId)) {
            result = LIKE.get(answerId).add(like);
            //只有添加成功才需要对记录进行更改
            if (result) {
                //移除掉取消点赞的记录
                boolean remove = removeLikes.remove(like);
                //如果删除失败，说明没有删除的记录，所以需要进行点赞的记录
                if (!remove) {
                    addLikes.add(like);
                }
            }
        } else {
            Set<SportAnswerLike> likes = new HashSet<>(CAPACITY);
            result = likes.add(like);
            LIKE.put(answerId, likes);
            addLikes.add(like);
        }
        return result;
    }

    @Override
    public boolean removeLike(String answerId, long uid) {
        //先进行持久化
        cacheManage();
        Set<SportAnswerLike> likes = LIKE.get(answerId);
        //若不存在这个集合，说明这个回复不存在，也不需要添加进记录
        if (likes != null) {
            SportAnswerLike like = new SportAnswerLike(answerId, uid);
            boolean remove = likes.remove(like);
            if (remove) {
                //取消点赞的记录
                Set<SportAnswerLike> removeLikes = CHANGE.get(false);
                //增加点赞的记录
                Set<SportAnswerLike> addLikes = CHANGE.get(true);
                //如果点赞记录里面有这个要被取消的点赞的记录，则删除它。如果没有的话，则在删除记录中添加这个要删除的记录
                if (!addLikes.remove(like)) {
                    removeLikes.add(like);
                }
            }
            return remove;
        }
        return false;
    }

    @Override
    public SportAnswerLike alreadyLike(String answerId, long uid) {
        Set<SportAnswerLike> likes = LIKE.get(answerId);
        SportAnswerLike like = null;
        if (likes != null) {
            boolean contains = likes.contains(new SportAnswerLike(answerId, uid));
            if (contains) {
                //存在则返回一个新创建的，避免集合中的信息被改动
                like = new SportAnswerLike(answerId, uid);
            }
        }
        return like;
    }

    /**
     * 持久化管理,如果非本类使用，则只能用于关闭webapp时进行持久化
     */
    public void cacheManage() {
        //查看是否已经超时
        long outOfTime = System.currentTimeMillis() - CACHE_SAVE_INTERVAL;
        if (outOfTime > lastTimeSave) {
            Set<SportAnswerLike> delLikes = CHANGE.get(false);
            Set<SportAnswerLike> addLikes = CHANGE.get(true);
            //持久化所有的要删除的点赞
            for (SportAnswerLike delLike : delLikes) {
                try {
                    LOGGER.warn(delLike + "正在删除...");
                    delete(delLike);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    commit();
                }
            }
            delLikes.clear();
            //持久化所有的要添加的点赞
            for (SportAnswerLike addLike : addLikes) {
                try {
                    LOGGER.warn(addLike + "正在插入...");
                    insert(addLike);
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    commit();
                }
            }
            addLikes.clear();
            lastTimeSave = System.currentTimeMillis();
        }

    }

    @Override
    public String getTableName() {
        return "sport_answer_like";
    }

    /**
     * 缓存持久化时间间隔
     */
    private static final long CACHE_SAVE_INTERVAL = Long.parseLong(ServiceConfUtils.getConfig("PostLikeCacheOutOfTime"));

    private static final Logger LOGGER = Logger.getLogger(SportAnswerLikeDaoImpl.class);
}
