package com.example.team_project.service.impl;

import com.example.team_project.dao.*;
import com.example.team_project.dao.impl.*;
import com.example.team_project.pojo.*;
import com.example.team_project.service.IslandService;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IslandServiceImpl implements IslandService {
    private final IslandDao ISLAND_DAO = new IslandDaoImpl();
    private final PostDao POST_DAO = new PostDaoImpl();
    private final PostConditionDao POST_CONDITION_DAO = new PostConditionDaoImpl();
    private final CommentDao COMMENT_DAO = new CommentDaoImpl();
    private final IslandMarkDao ISLAND_MARK_DAO = new IslandMarkDaoImpl();
    private static final Logger LOGGER = Logger.getLogger(IslandServiceImpl.class);

    @Override
    public boolean createIsland(long belong, String name, String introduce, byte type, int iconMark) {
        boolean result = false;
        try {
            int affect = ISLAND_DAO.createIsland(belong, name, introduce, type, iconMark);
            int mark = ISLAND_MARK_DAO.mark(new IslandMark(belong, belong));
            if (affect != 0 && mark != 0) {
                result = true;
            }
        } catch (Exception e) {
            ISLAND_DAO.rollback();
            ISLAND_MARK_DAO.rollback();
            result = false;
            System.out.println(e.getLocalizedMessage());
        } finally {
            ISLAND_DAO.commit();
            ISLAND_MARK_DAO.commit();
        }
        return result;
    }

    @Override
    public Icon getIslandIconByMark(int iconMark) {
        return ISLAND_DAO.getByMark(iconMark);
    }

    @Override
    public List<Icon> allIslandIcons(int currentPage, int count) {
        return ISLAND_DAO.getAll(currentPage - 1, count);
    }

    @Override
    public List<Icon> allIslandIcons() {
        return ISLAND_DAO.getAll();
    }

    @Override
    public boolean setIslandIcon(long belong, int iconMark) {
        boolean flag = false;
        //成功则提交，报错则回退
        try {
            int affect = ISLAND_DAO.setIcon(belong, iconMark);
            if (affect != 0) {
                flag = true;
            }
        } catch (Exception e) {
            ISLAND_DAO.rollback();
            e.printStackTrace();
        } finally {
            ISLAND_DAO.commit();
        }
        return flag;
    }

    @Override
    public Island hasIsland(long userId) {
        Island island = ISLAND_DAO.hasIsland(userId);
        if (island != null) {
            long markCount = ISLAND_MARK_DAO.getMarkCount(userId);
            //岛的id与用户的id一致
            island.setPopulation(markCount);
            LOGGER.debug("island:"+island+"\n获取成功");
        }
        return island;
    }

    @Override
    public boolean change(long belong, String name, String introduce, byte type, int iconMark) {
        boolean result = false;
        try {
            int affect = ISLAND_DAO.change(belong, name, introduce, type, iconMark);
            if (affect != 0) {
                result = true;
            }
        } catch (Exception e) {
            ISLAND_DAO.rollback();
            e.printStackTrace();
        } finally {
            ISLAND_DAO.commit();
        }
        return result;
    }

    @Override
    public List<Post> getPost(long belong, User user) {
        //获取所有的动态
        List<Post> all = POST_DAO.getPost(belong);
        //存放能够被访问的动态
        List<Post> result = new ArrayList<>();
        if (user != null) {
            //把可以访问的动态加入到列表中
            for (Post post : all) {
                if (post.getType() != 0) {
                    result.add(post);
                }else if (user.getId().equals(post.getPublisher()) || user.getId().equals(post.getBelong())) {
                    //若不为公开的，则判断登录的用户是否为动态发布者或者为岛主，只有这两者可以查看非公开动态
                    result.add(post);
                }
                //设置回复数量
                post.setReplyCount(COMMENT_DAO.commentCount(post.getId()));
                //设置用户情绪信息
                post.setCondition(POST_CONDITION_DAO.getPostCondition(post.getConditionNum()));
            }
        }else {
            //游客状态（未登录）只能查看公开的动态
            for (Post post : all) {
                if (post.getType() != 0) {
                    result.add(post);
                    post.setReplyCount(COMMENT_DAO.commentCount(post.getId()));
                }
            }
        }
        return result;
    }

    @Override
    public List<Post> getPostByPage(long belong, int currentPage, int count, User user) {
        //这里如果跟上面一样的处理方法，就无法保证条目数，只能从dao层进行改动
        List<Post> posts = POST_DAO.getPostByPage(belong, currentPage - 1, count, user);
        for (Post post : posts) {
            //设置回复数量
            post.setReplyCount(COMMENT_DAO.commentCount(post.getId()));
            //设置情绪信息
            post.setCondition(POST_CONDITION_DAO.getPostCondition(post.getConditionNum()));
        }
        return posts;
    }

    @Override
    public boolean release(long belong, String title, String body, long publisher, int condition, int type, boolean canComment) {
        boolean flag = false;
        long maxId = POST_CONDITION_DAO.maxId();
        //只有发布的岛和当前登录的用户是同一个人，且情绪不为0和超过最大值，才能够发布成功
        if (belong == publisher && condition > 0 && condition <= maxId) {
            try {
                int release = POST_DAO.release(belong, title, body, publisher, condition, type, canComment);
                if (release != 0) {
                    flag = true;
                }
            } catch (Exception e) {
                POST_DAO.rollback();
                e.printStackTrace();
            } finally {
                POST_DAO.commit();
            }
        }
        return flag;
    }

    @Override
    public long releasePostId(long belong, String title, long publisher) {
        return POST_DAO.getReleaseNowId(belong, title, publisher);
    }

    @Override
    public boolean delete(long belong,long id, long deleter) {
        boolean flag = false;
        //删除操作必须删除者是岛主
        if (belong == deleter) {
            //成功就同成功，失败则同失败
            try {
                //删除操作,先删除和动态有关的评论
                int delCommentCount = COMMENT_DAO.delCommentsByPid(id);
                if (delCommentCount != 0) {
                    LOGGER.info(id + "岛的评论删除成功，共删除了" + delCommentCount + "条");
                }
                //删除动态
                int delete = POST_DAO.delete(id, deleter);
                //若删除成功进行判断删除的是否为置顶动态
                if (delete != 0) {
                    flag = true;
                    //获取置顶动态id
                    Long topPost = getTopPost(belong);
                    if (topPost != null && topPost.equals(id)) {
                        //设置置顶动态为null
                        int affect = ISLAND_DAO.setNullTopPost(belong);
                        //删除的结果
                        if (affect == 0) {
                            flag = false;
                            LOGGER.info("岛" + belong + "的置顶" + topPost + "删除失败");
                        } else {
                            LOGGER.info("岛" + belong + "的置顶" + topPost + "删除成功");
                        }
                    }
                }
            } catch (Exception e) {
                POST_DAO.rollback();
                COMMENT_DAO.rollback();
                ISLAND_DAO.rollback();
                e.printStackTrace();
            } finally {
                POST_DAO.commit();
                COMMENT_DAO.commit();
                ISLAND_DAO.commit();
            }
        }
        return flag;
    }

    @Override
    public Post getOnePost(long postId) {
        Post post = POST_DAO.onePost(postId);
        post.setReplyCount(POST_DAO.getPostReplyCount(postId));
        post.setCondition(POST_CONDITION_DAO.getPostCondition(post.getConditionNum()));
        return post;
    }

    @Override
    public Post visit(long postId) {
        Post post = null;
        try {
            post = POST_DAO.onePost(postId);
            int affect = POST_DAO.addRead(postId);
            long count = POST_DAO.getPostReplyCount(postId);
            if (post == null && affect != 0) {
                System.out.println("文章获取失败!");
            }
            if (affect == 0 && post != null) {
                System.out.println("增加阅读量失败!");
            }
            if (post != null) {
                post.setReplyCount(count);
            }
        } catch (Exception e) {
            POST_DAO.rollback();
            e.printStackTrace();
        }finally {
            POST_DAO.commit();
        }
        return post;
    }

    @Override
    public long[] getIslandsIds(int current, int count) {
        return ISLAND_DAO.getIslandsIdByPage(current, count);
    }

    @Override
    public long islandsCount() {
        return ISLAND_DAO.islandsCount();
    }

    @Override
    public List<Island> getIslands(Long[] belongs) {
        List<Island> islands = null;
        if (belongs != null && belongs.length > 0) {
            long[] ids = new long[belongs.length];
            for (int i = 0; i < belongs.length; i++) {
                ids[i] = belongs[i];
            }
            islands = ISLAND_DAO.getIslandByIds(ids);
            for (Island island : islands) {
                island.setPopulation(ISLAND_MARK_DAO.getMarkCount(island.getBelong()));
            }
        }
        return islands;
    }

    @Override
    public long getMaxIid() {
        return ISLAND_DAO.getMaxIid();
    }

    @Override
    public long getIslandCount() {
        return ISLAND_DAO.islandsCount();
    }

    @Override
    public long getMinIid() {
        return ISLAND_DAO.getMinIid();
    }

    @Override
    public long postCount(long belong) {
        return POST_DAO.getPostCount(belong);
    }

    @Override
    public List<PostCondition> getAllCondition() {
        return POST_CONDITION_DAO.all();
    }

    @Override
    public PostCondition getOneCondition(int cid) {
        PostCondition condition = null;
        //减少错误数据访问数据库
        if (cid > 0) {
            condition = POST_CONDITION_DAO.getPostCondition(cid);
        }
        return condition;
    }

    @Override
    public Long getTopPost(long belong) {
        //从service层中减少对数据库的访问
        if (belong > 0) {
            long topPost = ISLAND_DAO.getTopPost(belong);
            if (topPost > 0) {
                return topPost;
            }
        }
        return null;
    }

    /**
     * 更改岛的置顶动态
     * @param belong 要更改置顶动态的岛
     * @param postId 要更改为的一个动态
     * @return 是否成功
     */
    @Override
    public boolean changeTopPost(long belong, Long postId) {
        boolean result = false;
        //判断岛id是否大于0，以及设置为置顶的帖子id是否大于0或为null
        if (belong > 0 && (postId == null || postId > 0)) {
            try {
                int affect = ISLAND_DAO.changeTopPost(belong, postId);
                if (affect != 0) {
                    result = true;
                }
                if (postId != null) {
                    Post post = POST_DAO.onePost(postId);
                    result = post.getBelong().equals(belong);
                }
            } catch (SQLException e) {
                ISLAND_DAO.rollback();
                e.printStackTrace();
            } finally {
                ISLAND_DAO.commit();
            }
        }
        return result;
    }

    public boolean removeTopPost(long belong) {
        boolean flag = false;
        if (belong > 0) {
            try {
                int result = ISLAND_DAO.setNullTopPost(belong);
                if (result != 0) {
                    flag = true;
                    LOGGER.info(belong + "岛的置顶已被删除");
                }else {
                    LOGGER.info(belong + "岛的置顶删除失败");
                }
            } catch (SQLException e) {
                ISLAND_DAO.rollback();
                e.printStackTrace();
            } finally {
                ISLAND_DAO.commit();
            }
        }
        return flag;
    }

    @Override
    public boolean hasRelease(long iid) {
        if (iid > 0) {
            return POST_DAO.hasRelease(iid);
        }else {
            return false;
        }
    }

    @Override
    public boolean hasMark(long uid, long iid) {
        if (uid > 0 && iid > 0) {
            return ISLAND_MARK_DAO.hasMark(new IslandMark(iid, uid));
        }else {
            return false;
        }
    }

    @Override
    public List<IslandMark> getMarks(long uid) {
        List<IslandMark> marks = null;
        if (uid > 0) {
            marks = ISLAND_MARK_DAO.getMark(uid);
        }
        return marks;
    }

    @Override
    public boolean mark(long uid, long iid) {
        boolean result = false;
        //减少数据库因为错误而增加的访问次数
        if (uid > 0 && iid > 0) {
            try {
                int mark = ISLAND_MARK_DAO.mark(new IslandMark(iid, uid));
                //若返回值大于0，则关注成功
                if (mark > 0) {
                    result = true;
                }
            } catch (SQLException e) {
                ISLAND_MARK_DAO.rollback();
                result = false;
                e.printStackTrace();
            }finally {
                ISLAND_MARK_DAO.commit();
            }
        }
        return result;
    }

    @Override
    public boolean delMark(long uid, long iid) {
        //与mark相似
        boolean result = false;
        if (uid > 0 && iid > 0 && uid != iid) {
            try {
                int affect = ISLAND_MARK_DAO.delMark(new IslandMark(iid, uid));
                if (affect > 0) {
                    result = true;
                }
            } catch (SQLException e) {
                ISLAND_MARK_DAO.rollback();
                result = false;
                e.printStackTrace();
            }finally {
                ISLAND_MARK_DAO.commit();
            }
        }
        return result;
    }

    @Override
    public long markCount(long iid) {
        if (iid > 0) {
            return ISLAND_MARK_DAO.getMarkCount(iid);
        }
        return 0;
    }

    @Override
    public long getMarkIslandCount(long uid) {
        if (uid > 0) {
            return ISLAND_MARK_DAO.getUserMarkCount(uid);
        }
        return 0;
    }

    @Override
    public List<Island> getMarkIslandIds(long uid, int currentPage, int count) {
        if (currentPage > 0 && count > 0 && uid > 0) {
            List<Long> markIslandIds = ISLAND_MARK_DAO.getMarkIslandIds(uid, currentPage - 1, count);
            //通过岛id获取岛
            return getIslands(markIslandIds.toArray(new Long[0]));
        }
        return null;
    }

    @Override
    public List<Island> getMarkIslandIds(long uid) {
        if (uid > 0) {
            List<Long> markIslandIds = ISLAND_MARK_DAO.getMarkIslandIds(uid);
            //通过岛id获取岛
            return getIslands(markIslandIds.toArray(new Long[0]));
        }
        return null;
    }

    @Override
    public boolean addRecord(long uid, long island) {
        boolean flag = false;
        //先删除后增加（有则为更新，无则为插入）
        if (uid > 0 && island > 0 && uid != island) {
            try {
                int del = ISLAND_DAO.delRecord(uid, island);
                ISLAND_DAO.commit();
                LOGGER.info(uid + "用户的浏览记录删除了"+del+"条");
                int add = ISLAND_DAO.addRecord(uid, island);
                LOGGER.debug("添加的条数为:" + add);
                if (add != 0) {
                    flag = true;
                    LOGGER.info(uid+"用户的浏览记录,修改成功");
                }
            } catch (SQLException e) {
                ISLAND_DAO.rollback();
                e.printStackTrace();
                LOGGER.warn(uid+"用户的浏览记录,修改失败");
            } finally {
                ISLAND_DAO.commit();
            }
        }
        return flag;
    }

    @Override
    public List<Island> getHasBeen(long uid, int size) {
        if (uid > 0 && size >= 0) {
            List<Long> hasBeen = ISLAND_DAO.getHasBeen(uid, size);
            List<Island> islands = getIslands(hasBeen.toArray(new Long[0]));
            if (islands != null) {
                LOGGER.info("要获取" + size + "条浏览记录，" + "获取到了" + hasBeen.size() + "个id，实际返回" + islands.size() + "个岛信息");
            }else {
                LOGGER.warn("要获取" + size + "条浏览记录，" + "获取到了" + hasBeen.size() + "个id,实际返回0个岛信息");
            }
            return islands;
        }
        LOGGER.warn("要获取浏览记录出错，uid = " + uid + ",size = " + size);
        return null;
    }

    @Override
    public long beenCount(long uid) {
        if (uid > 0) {
            return ISLAND_DAO.beenCount(uid);
        }
        return 0;
    }
}
