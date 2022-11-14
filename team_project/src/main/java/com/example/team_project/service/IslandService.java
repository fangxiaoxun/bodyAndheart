package com.example.team_project.service;

import com.example.team_project.pojo.*;

import java.sql.SQLException;
import java.util.List;

public interface IslandService {
    /**
     * 通过岛的相关参数，创建岛
     * @param belong
     * @param name
     * @param introduce
     * @param type
     * @param iconMark
     * @return 是否创建成功
     */
    boolean createIsland(long belong, String name, String introduce, byte type, int iconMark);


    /**
     * 根据iconMark获取岛的图像
     * @param iconMark
     * @return 岛的图像信息,找不到则为null
     */
    Icon getIslandIconByMark(int iconMark);

    /**
     * 获取岛的所有图片信息(分页)
     * @param currentPage 当前页码
     * @param count 一次展示的数量
     * @return 图片信息的集合,不为null
     */
    List<Icon> allIslandIcons(int currentPage, int count);

    /**
     * 同allIslandIcons(int currentPage, int count)，但不分页
     *
     */
    List<Icon> allIslandIcons();

    /**
     * 设置岛的图片
     * @param belong 岛的唯一标识
     * @param iconMark 设置的图片
     * @return 是否设置成功
     */
    boolean setIslandIcon(long belong, int iconMark);

    /**
     * 判断某个用户有没有岛
     * @param userId 要判断的用户的id
     * @return 返回island相关信息
     */
    Island hasIsland(long userId);

    /**
     * 改变岛的信息
     * @param belong 岛属于哪个用户（岛的标识）(用当前登录的用户的id)
     * @param name
     * @param introduce
     * @param type
     * @param iconMark
     * @return 改变成功为true，失败为false
     */
    boolean change(long belong, String name, String introduce, byte type, int iconMark);

    /**
     * 不分页获取文章
     * @param belong 文章属于哪个岛
     * @param user 访问的用户
     * @return 能被该用户访问的动态
     */
    List<Post> getPost(long belong, User user);

    /**
     * 获取岛内的帖子
     * @param belong 要获取哪个岛的文章
     * @return 文章的集合
     */
    List<Post> getPostByPage(long belong, int currentPage, int count, User user);

    /**
     * 发布帖子
     * @param belong 属于哪个岛
     * @param title 标题
     * @param body 正文
     * @param publisher 发布者（从session获取登录对象来获取）
     * @return 发布成功为true，失败为false
     */
    boolean release(long belong, String title, String body, long publisher, int condition, int type, boolean canComment);

    /**
     * 获取刚发布的帖子的id
     * @param belong 刚发布的帖子是属于哪个岛的？
     * @param title 文章的标题
     * @param publisher 发布帖子的用户（可以最大限度能够返回正确结果）
     * @return 返回刚发布的帖子的id
     */
    long releasePostId(long belong, String title, long publisher);

    /**
     * 删除帖子
     * @param id 帖子的id
     * @return 返回删除是否成功
     */
    boolean delete(long belong,long id,long deleter);

    /**
     * 获取一篇文章的信息
     * @param postId 帖子的id，唯一标识
     * @return 帖子的信息,若没有则返回null
     */
    Post getOnePost(long postId);

    /**
     * 访问文章
     * @param postId
     * @return 当有异常时，post可能会为null
     */
    Post visit(long postId);

    /**
     * 得到几个岛的id
     * @param current 当前页码 0开始
     * @param count 一次展示的条数
     * @return id的数组，只有当查询不到id时，才会为null
     */
    long[] getIslandsIds(int current, int count);

    /**
     * 获取所有岛的数量
     * @return 数量
     */
    long islandsCount();

    /**
     * 随机获取岛
     * @param belongs 要获取的岛的岛id,传多少个就是多少个
     * @return 岛的信息集合，不会为null,除非传null
     */
    List<Island> getIslands(Long[] belongs);

    /**
     * 获取最大的岛id
     * @return 最大的岛id
     */
    long getMaxIid();

    /**
     * 获取岛的数量
     * @return 岛的数量
     */
    long getIslandCount();

    /**
     * 获取最晓的岛id
     * @return 最小的岛id
     */
    long getMinIid();

    /**
     * 获取岛内文章的总数量
     * @param belong
     * @return
     */
    long postCount(long belong);

    /**
     * 获取所有的心情状态信息
     * @return
     */
    List<PostCondition> getAllCondition();

    /**
     * 获取单个情绪状态的详细信息
     * @param cid 情绪状态的id
     * @return 情绪状态的详细信息（若不存在，则为null）
     */
    PostCondition getOneCondition(int cid);

    /**
     * 获取岛的文章置顶
     * @param belong 要查询的岛
     * @return 若岛没有置顶，则会返回null
     */
    Long getTopPost(long belong);

    /**
     * 更改岛的置顶文章
     * @param islandId 岛的id
     * @param postId 要置顶的文章的id
     * @return 是否成功
     */
    boolean changeTopPost(long islandId, Long postId);

    /**
     * 移除置顶
     * @param belong 要删除置顶的岛
     * @return 返回是否删除成功
     */
    boolean removeTopPost(long belong);
    /**
     * 查询岛是否已经发布过内容
     * @param iid 查询的岛
     * @return 是否已经发布过
     */
    boolean hasRelease(long iid);

    /**
     * 查询用户是否已经关注过岛
     * @param uid 用户的id
     * @return 是否关注
     */
    boolean hasMark(long uid, long iid);

    /**
     * 查询用户关注过的岛
     * @param uid 要查询的用户
     * @return 用户关注过的岛，若一个没有关注，则返回长度为0的集合，参数异常返回null
     */
    List<IslandMark> getMarks(long uid);

    /**
     * 关注岛
     * @param uid 要进行关注操作的用户
     * @param iid 用户要关注的岛
     * @return 是否成功
     */
    boolean mark(long uid, long iid);

    /**
     * 取消关注
     * @param uid 要进行取消操作的用户
     * @param iid 用户要取消关注的岛
     * @return 是否成功
     */
    boolean delMark(long uid, long iid);

    /**
     * 岛被多少人关注
     * @param iid 要查看被关注次数的岛
     * @return 关注的次数
     */
    long markCount(long iid);

    /**
     * 求用户关注过多少岛，不包括自己的岛
     * @param uid 要查询的用户id
     * @return 返回关注数，不包括自己
     */
    long getMarkIslandCount(long uid);

    /**
     * 获取单个用户关注的所有岛的id
     * @param uid 要查询的用户
     * @param currentPage 目前的页数
     * @param count 一次展示的条目数
     * @return 关注过的所有岛的id
     */
    List<Island> getMarkIslandIds(long uid, int currentPage, int count);

    /**
     * 获取单个用户关注的所有岛的id，返回全部
     * @param uid 要查询的用户
     * @return 关注过的所有岛的id
     */
    List<Island> getMarkIslandIds(long uid);

    /**
     * 添加浏览记录
     * @param uid 要添加的用户
     * @param island 要添加的岛
     * @return 是否添加成功
     */
    boolean addRecord(long uid, long island);

    /**
     * 获取一定数量的浏览过的岛（最近浏览）
     * @param uid 要查看的用户
     * @param size 要获取的数量
     * @return 浏览过的岛的信息集合
     */
    List<Island> getHasBeen(long uid, int size);

    /**
     * 去过的岛的数量
     * @param uid 要查看的用户
     */
    long beenCount(long uid);
}
