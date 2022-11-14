package com.example.team_project.dao;

import com.example.team_project.framkwork.jdbc.repository.BaseMapper;
import com.example.team_project.pojo.Post;
import com.example.team_project.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface PostDao extends BaseMapper {
    /**
     * 获取帖子,按id逆序获取
     * @param island 要获取的帖子属于哪个岛
     * @param current 当前的页数
     * @param count 一次展示的数量
     * @param user 访问动态的用户
     * @return 帖子的内容的集合，若没有内容，List的size为0
     */
    List<Post> getPostByPage(long island, int current, int count, User user);

    /**
     * 不分页获取文章
     * @param island
     * @return
     */
    List<Post> getPost(long island);

    /**
     * 在岛中发布帖子
     * @param belong 帖子属于哪个岛
     * @param title 帖子标题
     * @param body 帖子内容
     * @param publisher 发布者id
     * @param condition 发帖人心情状态
     * @param type 公开还是私密
     * @return 发布成功为1，失败为0
     */
    int release(long belong, String title, String body,long publisher, int condition, int type, boolean canComment) throws SQLException;

    /**
     * 删除帖子
     * @param id 删除的帖子id
     * @return 删除成功为1，失败为0
     */
    int delete(long id, long deleter) throws SQLException;

    /**
     * 获取特定的一篇帖子
     * @param postId 帖子的唯一标识id
     * @return 帖子的信息，可能为null
     */
     Post onePost(long postId);

    /**
     * 增加阅读量
     * @param postId 帖子的唯一标识id
     * @return 增加成功或失败
     */
     int addRead(long postId) throws SQLException;


    /**
     * 获取单个动态回复数量
     * @param postId 动态的id
     * @return 回复数量，没有就为0，有则为有的数量
     */
     long getPostReplyCount(long postId);

    /**
     * 获取多个动态的动态回复数量（目的：减少sql请求次数）
     * <p>必须由大到小传</p>
     * @param postId 不定数量的动态id
     * @return <p>传进多少个动态id，则数组的长度就为多少，回复数量逐一对应</p><p>回复数量，没有就为0，有则为有的数量</p>
     *<p>若一个都没有传则返回null</p>
     */
     long[] getPostReplyCount(long... postId);

    /**
     * 获取一共有多少帖子
     * @return
     */
     long getPostCount(long belong);

    /**
     * 获取刚发布的帖子的id
     * @param belong 属于哪个岛
     * @param title 标题
     * @param publisher 发布人是谁
     * @return 某个岛中，属于这个发布人的最后的一条帖子
     */
     long getReleaseNowId(long belong, String title, long publisher);

    /**
     * 查看这个岛，是否已经发过贴
     * @param iid 要查看的岛
     * @return 是否已经发布过
     */
     boolean hasRelease(long iid);

}
