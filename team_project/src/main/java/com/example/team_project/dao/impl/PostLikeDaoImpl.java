package com.example.team_project.dao.impl;

import com.example.team_project.dao.PostLikeDao;
import com.example.team_project.framkwork.jdbc.repository.AbsBaseMapper;
import com.example.team_project.pojo.PostLike;
import com.example.team_project.utils.ServiceConfUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * 动态点赞相关的数据库操作，使用了缓存的思想
 */
public class PostLikeDaoImpl extends AbsBaseMapper implements PostLikeDao {
    /**
     * 点赞的缓存,避免多次读写数据库
     */
    private static final HashMap<Long, HashSet<PostLike>> CACHE_EXIST = new HashMap<>();
    /**
     * 用户上一次访问缓存的时间
     */
    private static final HashMap<Long, Long> TIME_CACHE = new HashMap<>();
    /**
     * 最后一次更新的时间
     */
    private static Long lastUpdateTime = System.currentTimeMillis();
    /**
     * 文章id为key，文章的点赞数为value
     */
    private static final HashMap<Long,Long> LIKE_COUNT = new HashMap<>();

    @Override
    public boolean alreadyLike(long postId, long uid) {
        boolean flag = false;
        //触发缓存管理
        try {
            cacheManage(uid);
        } catch (SQLException e) {
            try {
                jdbcUtils.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                jdbcUtils.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //创建出用于对比的对象
        PostLike condition = new PostLike();
        condition.setLiker(uid);
        condition.setPostId(postId);
        //从缓存中拿出相关数据
        HashSet<PostLike> postLikes = CACHE_EXIST.get(uid);
        if (postLikes != null) {
            //检测缓存中是否有这个数据
            boolean contains = postLikes.contains(condition);
            if (contains) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean[] alreadyLikes(long uid, long... postId) {
        //存放结果
        boolean[] result = new boolean[postId.length];
        for (int i = 0; i < postId.length; i++) {
            result[i] = alreadyLike(postId[i], uid);
        }
        return result;
    }

    /**
     * 缓存管理
     * @param uid 读取的点赞信息的uid
     */
    private void cacheManage(long uid) throws SQLException {
        long now = System.currentTimeMillis();
        //超时时间(读取配置获得,默认8分钟)
        long outOfTime = now - CACHE_OUT_OF_TIME;
        //如果上次更新已经是超时的，则进行更新数据库
        if (lastUpdateTime < outOfTime) {
            //遍历超时数据，并移除，超时数据一般是目前不需要的了，使用一次会刷新时间
            //使用迭代器，才能对TIME_CACHE数据进行删除操作
            Iterator<Map.Entry<Long, Long>> iterator = TIME_CACHE.entrySet().iterator();
            Map.Entry<Long, Long> entry;
            while (iterator.hasNext()) {
                //从迭代器中获取Entry对象
                entry = iterator.next();
                Long value = entry.getValue();
                if (value < outOfTime && !entry.getKey().equals(uid)) {
                    //通过TIME_CACHE的时间来判断哪个用户的数据已经超过时间了
                    //进行更新并移除不需要的数据
                    HashSet<PostLike> postLikes = CACHE_EXIST.get(entry.getKey());
                    //把数据修改进数据库
                    int update = updateByCache(postLikes, entry.getKey());
                    if (update != 0) {
                        System.out.println("进行了修改，且set有" + postLikes.size() + "条记录");
                    } else {
                        System.out.println("没有进行修改，且set有" + postLikes.size() + "条记录");
                    }
                    CACHE_EXIST.remove(entry.getKey());
                    iterator.remove();
                }
            }
            //把最后一次更新的时间更新为删除的时间
            lastUpdateTime = now;
        }
        //如果缓存中不存在这个用户的点赞数据，则进行加载
        if (!CACHE_EXIST.containsKey(uid)) {
            loadData(uid);
        }
        //使用后更新使用时间
        TIME_CACHE.put(uid, System.currentTimeMillis());
    }

    /**
     * 从数据库加载某位用户的数据
     * @param uid 要加载的用户
     */
    private void loadData(long uid) {
        PostLike like = new PostLike();
        like.setLiker(uid);
        List<PostLike> select = select(like);
        CACHE_EXIST.put(uid, new HashSet<>(select));
    }

    /**
     * 缓存更新数据库
     */
    private int updateByCache(HashSet<PostLike> postLikes, Long uid) throws SQLException {
        int affect = 0;
        PostLike condition = new PostLike();
        condition.setLiker(uid);
        //可能死锁，要测试下，测试完发现，如果不先commit的话，确实会导致下面锁住
        //这样的话每隔超时时间IO压力可能都比较大，比较稳妥的方式可能是加一个删除的缓存和一个添加的缓存，但是写起来会比较复杂
        //先暂且不做修改
        affect = delete(condition);
        System.out.println("删除" + condition);
        System.out.println("删除" + affect + "条");
        //不释放的话，会因为操作相同数据被阻塞
        jdbcUtils.commit();
        if (postLikes.size() > 0) {
            StringBuilder sql = new StringBuilder("INSERT INTO post_like(post_id, liker) values ");
            sql.append("(?,?)");
            for (int i = 1; i < postLikes.size(); i++) {
                sql.append(", (?,?)");
            }
            System.out.println(sql);
            Object[] params = new Object[postLikes.size() * 2];
            int index = 0;
            for (PostLike postLike : postLikes) {
                params[index++] = postLike.getPostId();
                params[index++] = postLike.getLiker();
            }
            affect = jdbcUtils.insert(sql.toString(), params);
            System.out.println(Arrays.toString(postLikes.toArray()));
            System.out.println("添加" + affect + "条");
        }
        return affect;
    }

    /**
     * 需要先加载缓存
     * @param uid    点赞的用户
     * @param postId 点赞的动态
     * @return 点赞成功或失败
     */
    @Override
    public boolean like(long uid, long postId) {
        PostLike like = new PostLike();
        like.setPostId(postId);
        like.setLiker(uid);
        HashSet<PostLike> postLikes = CACHE_EXIST.get(uid);
        //若不存在该用户的数据，则加载进缓存
        if (postLikes == null) {
            loadDataWhenNeed(uid);
            //重新获取
            postLikes = CACHE_EXIST.get(uid);
        }
        if (postLikes.contains(like)) {
            return false;
        } else {
            boolean result = postLikes.add(like);
            //添加成功则给点赞数+1
            if (result) {
                addLikeCount(postId);
            }
            return result;
        }
    }

    /**
     * 需要先加载缓存
     * @param postId 删除的评论在哪个帖子
     * @param uid    删除的评论是哪个用户的
     * @return
     */
    @Override
    public boolean delete(Long postId, Long uid) {
        //获取该用户对应的点赞数据
        HashSet<PostLike> postLikes = CACHE_EXIST.get(uid);
        //封装要删除的postLike信息
        PostLike postLike = new PostLike();
        postLike.setLiker(uid);
        postLike.setPostId(postId);
        //当数据不存在时加载数据
        if (postLikes == null) {
            loadDataWhenNeed(uid);
            //再次获取数据
            postLikes = CACHE_EXIST.get(uid);
        }
        //若存在这个postLike，则删除并且返回true，删除成功，否则删除失败
        if (postLikes.contains(postLike)) {
            boolean result = postLikes.remove(postLike);
            //取消成功则给点赞数减去1
            if (result) {
                delLikeCount(postId);
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * 暴力持久化，当webapp被关闭前才可以被执行
     */
    public void forcePersistence() {
        for (Map.Entry<Long, HashSet<PostLike>> entry : CACHE_EXIST.entrySet()) {
            try {
                updateByCache(entry.getValue(), entry.getKey());
            } catch (Exception e) {
                try {
                    jdbcUtils.rollback();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    jdbcUtils.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在增加和删除时，数据已经超时被清除所做的操作
     * @param uid 要加载点赞信息的用户id
     */
    private void loadDataWhenNeed(long uid) {
        //加载数据
        loadData(uid);
        //在最后一次使用的时间缓存中，增加这次加入的记录
        TIME_CACHE.put(uid,System.currentTimeMillis());
    }

    /**
     * 获取点赞数量
     * @param pid 要查看数量的岛
     * @return 点赞数
     */
    @Override
    public long likeCount(long pid) {
        loadLikeCount(pid);
        return LIKE_COUNT.get(pid);
    }

    /**
     * 加载点赞数量
     * @param pid postId，要获取点赞数量的帖子
     */
    private void loadLikeCount(long pid) {
        if (!LIKE_COUNT.containsKey(pid)) {
            PostLike postLike = new PostLike();
            postLike.setPostId(pid);
            long count = count(postLike);
            LIKE_COUNT.put(pid, count);
        }
    }

    /**
     * 添加点赞数
     * @param pid 点赞的帖子的id
     */
    private void addLikeCount(long pid) {
        loadLikeCount(pid);
        LIKE_COUNT.put(pid, LIKE_COUNT.get(pid) + 1);
    }

    /**
     * 减少点赞数
     * @param pid 取消点赞的帖子id
     */
    private void delLikeCount(long pid) {
        loadLikeCount(pid);
        LIKE_COUNT.put(pid,LIKE_COUNT.get(pid) - 1);
    }

    @Override
    public String getTableName() {
        return "post_like";
    }

    private static final long CACHE_OUT_OF_TIME = Long.parseLong(ServiceConfUtils.getConfig("PostLikeCacheOutOfTime"));
}
