package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.*;
import com.example.team_project.service.CommentService;
import com.example.team_project.service.IslandService;
import com.example.team_project.service.IslandVoiceService;
import com.example.team_project.service.PostLikeService;
import com.example.team_project.service.impl.CommentServiceImpl;
import com.example.team_project.service.impl.IslandServiceImpl;
import com.example.team_project.service.impl.PostLikeServiceImpl;
import com.example.team_project.utils.ServiceConfUtils;
import com.fasterxml.jackson.datatype.jsr310.deser.key.ZoneIdKeyDeserializer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@WebServlet("/island/*")
public class IslandServlet extends BaseServlet {
    private static final String PATH = ServiceConfUtils.getConfig("HOST");
    private IslandService service = new IslandServiceImpl();
    private CommentService commentService = new CommentServiceImpl();
    private PostLikeService postLikeService = new PostLikeServiceImpl();
    private IslandVoiceService voiceService = BeanFactory.getBean("islandVoiceService", IslandVoiceService.class);

    /**
     * 获取岛的所有图片
     */
    public void getAllIcons(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String page = request.getParameter("currentPage");
        String countNum = request.getParameter("count");
        List<Icon> icons;
        ApiMsg msg;
        //判空
        if (page != null && countNum != null && !page.trim().equals("") && !countNum.trim().equals("")) {
            int currentPage = Integer.parseInt(page);
            int count = Integer.parseInt(countNum);
            //判断分页条件是否符合规范
            if (count > 0 && currentPage > 0) {
                icons = service.allIslandIcons(currentPage, count);
                msg = ApiMsg.ok();
            } else {
                icons = service.allIslandIcons();
                msg = new ApiMsg(200, "all");
            }
        } else {
            icons = service.allIslandIcons();
            msg = new ApiMsg(200, "all");
        }
        //判断是否有数据
        if (icons.size() != 0) {
            //设置为绝对路径
            IconServlet.setIconsPath(icons, request.getContextPath());
            msg.setData(MapUtils.getMap("icons", icons));
        } else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 设置岛的图片
     */
    public void setIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String belong = request.getParameter("belong");
        String iconMark = request.getParameter("iconMark");
        ApiMsg msg;
        //判空
        if (belong != null && iconMark != null && !belong.trim().equals("") && !iconMark.trim().equals("")) {
            long belongId = Long.parseLong(belong);
            int mark = Integer.parseInt(iconMark);
            //判断数据是否有效
            if (belongId > 0 && mark > 0) {
                boolean result = service.setIslandIcon(belongId, mark);
                if (result) {
                    //设置成功，响应结果为success
                    msg = new ApiMsg(200, "success");
                } else {
                    //设置失败，响应结果为fail
                    msg = new ApiMsg(200, "fail");
                }
            } else {
                //数据无效
                msg = ApiMsg.error();
            }
        } else {
            //数据为空
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 用户是否具有岛，有则返回岛的相关数据，没有则为null
     */
    public void hasIsland(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            Island island = service.hasIsland(user.getId());
            if (island != null) {
                msg = new ApiMsg(200, "yes");
                msg.setData(MapUtils.getMap("island", island));
            } else {
                msg = new ApiMsg(200, "no");
            }
        } else {
            msg = new ApiMsg(200, "noLogin");
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 创建岛
     */
    public void createIsland(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String belongs = request.getParameter("belong");
        String islandName = request.getParameter("name");
        String introduce = request.getParameter("intro");
        String typeValue = request.getParameter("type");
        String iconMark = request.getParameter("iconMark");
        ApiMsg msg;
        //判空
        if (belongs != null && islandName != null && introduce != null && typeValue != null && iconMark != null) {
            long belong = Long.parseLong(belongs);
            //1代表公开 0代表不公开
            byte type = Byte.parseByte(typeValue);
            int mark = Integer.parseInt(iconMark);
            if (belong > 0 && type >= 0 && mark > 0) {
                boolean result = service.createIsland(belong, islandName, introduce, type, mark);
                if (result) {
                    //创建成功
                    msg = new ApiMsg(200, "success");
                } else {
                    //创建失败
                    msg = new ApiMsg(200, "fail");
                }
            } else {
                msg = ApiMsg.error();
            }
        } else {
            msg = ApiMsg.error();
        }
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取某个岛的图片
     */
    public void getIconByMark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String mark = request.getParameter("iconMark");
        boolean flag = false;
        ApiMsg msg;
        Icon icon = null;
        //判空
        if (mark != null && !mark.trim().equals("")) {
            int iconMark = Integer.parseInt(mark);
            if (iconMark > 0) {
                icon = service.getIslandIconByMark(iconMark);
                //用于判断是否成功获取
                if (icon != null) {
                    icon.setIconUrl(PATH + request.getContextPath() + icon.getIconUrl());
                    flag = true;
                }
            }
        }
        if (flag) {
            msg = ApiMsg.ok();
            msg.setData(MapUtils.getMap("icon", icon));
        } else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 修改岛的信息
     */
    public void changeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String islandName = request.getParameter("name");
        String intro = request.getParameter("intro");
        String typeValue = request.getParameter("type");
        String iconMark = request.getParameter("iconMark");
        ApiMsg msg = null;
        //判空
        if (intro != null && islandName != null && typeValue != null && iconMark != null
                && !intro.trim().equals("") && !typeValue.trim().equals("")
                && !iconMark.trim().equals("")) {
            byte type = Byte.parseByte(typeValue);
            int mark = Integer.parseInt(iconMark);
            if (type >= 0 && mark > 0) {
                //只有岛的主人有修改岛的权利，这里可以保证是岛主修改自己的岛
                User user = (User) request.getSession().getAttribute("user");
                if (user != null) {
                    boolean change = service.change(user.getId(), islandName, intro, type, mark);
                    if (change) {
                        //修改成功
                        msg = new ApiMsg(200, "success");
                    } else {
                        //修改失败
                        msg = new ApiMsg(200, "fail");
                    }
                } else {
                    //未登录
                    msg = new ApiMsg(200, "userNotLogin");
                }
            }
        }
        if (msg == null) {
            msg = ApiMsg.error();
        }
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 随机获取岛
     */
    public void getIslandsRandom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String countNum = request.getParameter("count");
        User user = (User) request.getSession().getAttribute("user");
        final int DEFAULT = 5;
        ApiMsg msg = null;
        //是否缺少参数
        boolean flag = true;
        if (countNum != null) {
            int count = Integer.parseInt(countNum);
            if (count > 0) {
                List<Island> islands = randomIslands(count,user);
                flag = false;
                msg = ApiMsg.ok(MapUtils.getMap("islands", islands));
            }
        }
        if (flag) {
            List<Island> islands = randomIslands(DEFAULT,user);
            msg = new ApiMsg(200, "default");
            msg.setData(MapUtils.getMap("islands", islands));
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取岛中的帖子，有分页参数为分页，没有为全部获取，不推荐全部获取
     */
    public void getPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = request.getParameter("currentPage");
        String count = request.getParameter("count");
        String belong = request.getParameter("belong");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (belong != null && !belong.trim().equals("")) {
            long id = Long.parseLong(belong);
            if (id > 0) {
                List<Post> posts;
                if (page != null && count != null && !page.trim().equals("") && !count.trim().equals("")) {
                    int current = Integer.parseInt(page);
                    int num = Integer.parseInt(count);
                    if (num > 0 && current > 0) {
                        posts = service.getPostByPage(id, current, num, user);
                        msg = ApiMsg.ok(MapUtils.getMap("posts", posts));
                    } else {
                        //分页条件错误则全部获取
                        posts = service.getPost(id,user);
                        msg = new ApiMsg(200, "all");
                        msg.setData(MapUtils.getMap("posts", posts));
                    }
                } else {
                    //分页条件为空，全部获取
                    posts = service.getPost(id,user);
                    msg = new ApiMsg(200, "all");
                    msg.setData(MapUtils.getMap("posts", posts));
                }
                //把情绪状态的路径设置为绝对路径
                for (Post post : posts) {
                    PostCondition condition = post.getCondition();
                    if (condition != null) {
                        condition.setUrl(PATH + request.getContextPath() + condition.getUrl());
                    }
                }
            } else {
                //岛的标识有误
                msg = ApiMsg.error();
            }
        } else {
            //岛的标识有误
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 访问一篇文章，然后把文章的read+1
     */
    public void visit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        String articleId = request.getParameter("articleId");
        ApiMsg msg;
        if (articleId != null && !articleId.trim().equals("")) {
            Post visit = service.visit(Long.parseLong(articleId));
            if (visit != null) {
                msg = ApiMsg.ok(MapUtils.getMap("visit", visit));
            } else {
                msg = new ApiMsg(200, "postIsNull");
            }
        } else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 发布帖子
     */
    public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        //获取参数
        String belong = request.getParameter("belong");
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        //发动态时的心情
        String condition = request.getParameter("condition");
        String type = request.getParameter("type");
        String canComment = request.getParameter("canComment");
        User user = (User) request.getSession().getAttribute("user");
        LOGGER.debug("condition = "+ condition);
        LOGGER.debug(user+"发布动态在 " + belong + "岛");
        ApiMsg msg = null;
        //查看登录状态
        if (user != null) {
            //判空操作
            if (belong != null && title != null && body != null && !belong.trim().equals("") && !title.trim().equals("") && !body.trim().equals("")) {
                //以下三个属性都具有默认值，当没有这个参数时，则采用默认值
                int conditionNum;
                int typeNum;
                boolean can;
                if (condition == null) {
                    //虽然有默认值，但是这个心情，不推荐被默认
                    conditionNum = 8;
                }else {
                    conditionNum = Integer.parseInt(condition);
                }
                if (type == null) {
                    //默认为公开
                    typeNum = 1;
                }else {
                    typeNum = Integer.parseInt(type);
                }
                if (canComment == null) {
                    can = true;
                }else {
                    can = Boolean.parseBoolean(canComment);
                }
                long isId = Long.parseLong(belong);
                //发布只能发布在自己岛上，即使直接调接口，也是一样
                boolean release = service.release(isId, title, body, user.getId(),conditionNum,typeNum,can);
                if (release) {
                    //发布成功
                    msg = new ApiMsg(200, "success");
                    msg.setData(MapUtils.getMap("postId",service.releasePostId(isId,title, user.getId())));
                } else {
                    //发布失败
                    msg = new ApiMsg(200, "fail");
                }
            }
        } else {
            //未登录的标志
            msg = ApiMsg.notLogin();
        }
        if (msg == null) {
            //参数异常
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 删贴
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        String id = request.getParameter("postId");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg = null;
        //判断是否登录
        if (user != null) {
            //判断关键参数是否为null
            if (id != null) {
                long articleId = Long.parseLong(id);
                //判断是否有意义
                if (articleId > 0) {
                    //目前只有岛主可以发帖删帖，所以属于的岛，目前可以直接写死为用户的id
                    boolean delete = service.delete(user.getId(), articleId, user.getId());

                    if (delete) {
                        //删除成功
                        msg = new ApiMsg(200, "success");
                    } else {
                        //删除失败
                        msg = new ApiMsg(200, "fail");
                    }
                }
            }
        } else {
            //还没登录
            msg = ApiMsg.notLogin();
        }
        if (msg == null) {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 修改置顶动态
     */
    @ContentType
    public void setTop(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String islandId = request.getParameter("belong");
        String postId = request.getParameter("postId");
        ApiMsg msg = null;
        Long pid = null;
        User user = (User) request.getSession().getAttribute("user");
        //若未登录，返回未登录消息
        if (user == null) {
            mapper.writeValue(response.getOutputStream(), ApiMsg.notLogin());
            return;
        }
        if (islandId != null && !islandId.trim().equals("")) {
            boolean result = false;
            long iid = Long.parseLong(islandId);
            //判断岛的id是否与用户的id相同
            if (user.getId() == iid) {
                if (postId != null && !postId.trim().equals("")) {
                    pid = Long.parseLong(postId);
                    //有postId为设置置顶
                    result = service.changeTopPost(iid, pid);
                } else {
                    //如果没有传postId，则为取消置顶
                    result = service.removeTopPost(iid);
                }
            }
            if (result) {
                //接口返回的值，若为设置，则返回设置为置顶的帖子内容，若为取消，则返回null
                Post onePost = null;
                if (pid != null) {
                    onePost = service.getOnePost(pid);
                    PostCondition condition = onePost.getCondition();
                    //情绪路径设置为绝对路径
                    condition.setUrl(PATH + request.getContextPath() + condition.getUrl());
                }
                //设置成功并返回删除成功的消息，且附带置顶动态的id
                msg = new ApiMsg(200,"success");
                //或许可以改成返回置顶动态的内容
                msg.setData(MapUtils.getMap("topPost",onePost));
            }else {
                //删除失败返回的消息
                msg = new ApiMsg(200, "fail");
            }
        }else {
            //参数缺失
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 展示岛的信息,包括置顶
     */
    public void showIsland(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        String islandId = request.getParameter("islandId");
        ApiMsg msg;
        if (islandId != null && !islandId.trim().equals("")) {
            long id = Long.parseLong(islandId);
            //获取岛的信息
            Island island = service.hasIsland(id);
            if (island != null) {
                Map<String, Object> value = MapUtils.getMap("island", island);
                //为了把置顶动态也放进去响应消息里
                Post top = null;
                if (island.getTopPost() != null) {
                    top = service.getOnePost(island.getTopPost());
                }
                value.put("topPost", top);
                value.put("postCount", service.postCount(island.getBelong()));
                msg = ApiMsg.ok(value);
                User user = (User) request.getSession().getAttribute("user");
                if (user != null) {
                    service.addRecord(user.getId(), id);
                }
            } else {
                //岛没有找到
                msg = new ApiMsg(200, "notFound");
            }
        } else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 随机获取岛
     * @param count 条目数
     * @return 随机的岛（不为自己的）
     */
    private List<Island> randomIslands(int count, User user) {
        //用set集合保证不重复
        Set<Long> iids = new HashSet<>(count);
        //获取最大岛id
        long maxIid = service.getMaxIid();
        List<Island> islands;
        if (maxIid == 0) {
            LOGGER.warn("岛的最大id为0，可能数据库中没有岛的存在");
            return null;
        }else {
            long islandsCount = service.islandsCount();
            long[] islandsIds = service.getIslandsIds(0, (int) islandsCount);
            //加一是为了保证除了自己的，至少有count个
            if (islandsCount > count + 1) {
                //保证拿到的不重复
                while (iids.size() < count) {
                    int index = (int) (Math.random() * islandsCount);
                    //不能把用户自己的岛的id加入到set集合中
                    if (user == null || islandsIds[index] != user.getId()) {
                        iids.add(islandsIds[index]);
                    }
                }
            }else {
                if (user != null) {
                    long uid = user.getId();
                    for (long islandsId : islandsIds) {
                        //保证用户的岛id不会被添加
                        if (islandsId != uid) {
                            iids.add(islandsId);
                        }
                    }
                }else {
                    //若为游客登录，则全部展示
                    for (long islandsId : islandsIds) {
                        iids.add(islandsId);
                    }
                }
            }
            islands = service.getIslands(iids.toArray(new Long[0]));
        }
        return islands;
    }

    /**
     * 获得动态的评论消息
     */
    public void getComments(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //设置响应头
        response.setContentType("application/json;charset=utf-8");
        String id = request.getParameter("postId");
        String page = request.getParameter("currentPage");
        String count = request.getParameter("count");
        List<Comment> comments;
        ApiMsg msg = null;
        //若参数齐全，则分页获取
        //若参数缺少，但id不缺少，则全部获取
        if (id != null && page != null && count != null && !id.trim().equals("") && !page.trim().equals("") && !count.trim().equals("")) {
            comments = commentService.getCommentsByPage(Long.parseLong(id),Integer.parseInt(page),Integer.parseInt(count));
            msg = ApiMsg.ok(MapUtils.getMap("comments",comments));
        }else if (id != null && !id.trim().equals("")){
            comments = commentService.getComments(Long.parseLong(id));
            msg = new ApiMsg(200,"all");
            msg.setData(MapUtils.getMap("comments",comments));
        }
        //如果消息为null，则为id参数缺少，返回error
        if (msg == null) {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 发布动态下的评论
     */
    public void releaseComment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取发布在哪个帖子下
        String id = request.getParameter("postId");
        //获取正文
        String body = request.getParameter("body");
        //获取它的父评论，若没有父评论则为null
        String father = request.getParameter("father");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        LOGGER.debug("id = " + id);
        LOGGER.debug("body = " + body);
        LOGGER.debug(user + "发布评论");
        //只有已经登录的人才可以发表评论
        if (user != null) {
            if (id != null && !id.trim().equals("") && body != null && !body.trim().equals("")) {
                Comment comment = new Comment();
                //单独处理子评论，给它设置上它的父评论
                if (father != null && !father.trim().equals("")) {
                    comment.setFatherId(Long.parseLong(father));
                }
                //父子评论都相同的部分
                comment.setDiscussant(user.getId());
                comment.setBody(body);
                long pid = Long.parseLong(id);
                comment.setPostId(pid);
                if (commentService.release(comment)) {
                    //发布成功
                    msg = new ApiMsg(200,"success");
                    LOGGER.debug("发布成功!");
                    msg.setData(MapUtils.getMap("comments",commentService.getComments(pid)));
                }else {
                    //发布失败
                    msg = new ApiMsg(200,"fail");
                    LOGGER.debug("发布失败!");
                }
            }else {
                //参数异常
                msg = ApiMsg.error();
            }
        }else {
            //未登录
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 批量查看是否已经点赞了动态
     */
    public void alreadyLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        //获取用户是否已经登录，未登录则直接返回未登录信息
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg = null;
        //若无用户，直接返回未登录消息
        if (user != null) {
            String[] id = request.getParameterValues("postId");
//            获取到的数组不能为空
            if (id != null && id.length > 0) {
                long[] ids = new long[id.length];
                for (int i = 0; i < id.length; i++) {
                    ids[i] = Long.parseLong(id[i]);
                }
                //获取每一个都是否有点赞
                boolean[] already = postLikeService.alreadyLikes(user.getId(), ids);

                msg = ApiMsg.ok(MapUtils.getMap("postLikes",already));
            }
        } else {
            msg = ApiMsg.notLogin();
        }
        //若到这还未被赋值,则信息返回error
        if (msg == null) {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void alreadyLikeOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("postId");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        //查看登录状态
        if (user != null) {
            if (id != null) {
                boolean result = postLikeService.alreadyLike(user.getId(), Long.parseLong(id));
                Map<String, Object> value = new HashMap<>(2);
                value.put("result",result);
                value.put("likeCount",postLikeService.likeCount(Long.parseLong(id)));
                msg = ApiMsg.ok(value);
            } else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 取消点赞
     */
    public void deleteLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg = null;
        if (user != null) {
            String id = request.getParameter("postId");
            if (id != null && !id.trim().equals("")) {
                long pid = Long.parseLong(id);
                boolean delete = postLikeService.delete(user.getId(), pid);
                if (delete) {
                    msg = new ApiMsg(200,"success");
                }else {
                    msg = new ApiMsg(200,"fail");
                }
                //放入当前点赞数
                msg.setData(MapUtils.getMap("likeCount",postLikeService.likeCount(pid)));
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        if (msg == null) {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 点赞
     */
    public void like(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg = null;
        if (user != null) {
            String id = request.getParameter("postId");
            if (id != null && !id.trim().equals("")) {
                long pid = Long.parseLong(id);
                boolean like = postLikeService.like(user.getId(), pid);
                if (like) {
                    msg = new ApiMsg(200,"success");
                }else {
                    msg = new ApiMsg(200,"fail");
                }
                //放入当前点赞的数量
                msg.setData(MapUtils.getMap("likeCount",postLikeService.likeCount(pid)));
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        if (msg == null){
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 获取所有的情绪状态信息
     */
    public void allConditions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        List<PostCondition> conditions = service.getAllCondition();
        //前端要18个，共有20个，此处改为18个
        conditions = conditions.subList(0,18);
        StringBuilder url = new StringBuilder();
        String contextPath = request.getContextPath();
        for (PostCondition condition : conditions) {
            //设置condition图片的路径为绝对路径
            url.append(PATH).append(contextPath).append(condition.getUrl());
            condition.setUrl(url.toString());
            //清空，继续下一次循环
            url.delete(0,url.length());
        }
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("conditions",conditions));
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取单个情绪状态信息
     */
    @ContentType
    public void oneCondition(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取情绪的id
        String cid = request.getParameter("cid");
        ApiMsg msg;
        if (cid != null) {
            PostCondition condition = service.getOneCondition(Integer.parseInt(cid));
            if (condition != null) {
                condition.setUrl(PATH + request.getContextPath() + condition.getUrl());
                msg = ApiMsg.ok(MapUtils.getMap("condition",condition));
            }else {
                msg = new ApiMsg(200,"notFound");
            }
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取岛内是否已经发过帖子
     */
    @ContentType
    public void hasPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("islandId");
        ApiMsg msg;
        if (id != null) {
            boolean result = service.hasRelease(Long.parseLong(id));
            msg = ApiMsg.ok(MapUtils.getMap("hasRelease",result));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 当前登录的用户是否已经关注过本岛
     */
    @ContentType
    public void hasMark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String id = request.getParameter("islandId");
        ApiMsg msg;
        //判断是否为登录状态，只有登录时，才能使用这个功能
        if (user != null) {
            //判空
            if (id != null) {
                boolean result = service.hasMark(user.getId(), Long.parseLong(id));
                msg = ApiMsg.ok(MapUtils.getMap("hasMark", result));
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 当前用户关注岛
     */
    @ContentType
    public void mark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String id = request.getParameter("islandId");
        ApiMsg msg;
        //判断是否为登录状态，只有登录时，才能使用这个功能
        if (user != null) {
            //判空
            if (id != null) {
                long iid = Long.parseLong(id);
                boolean result = service.mark(user.getId(), iid);
                if (result) {
                    msg = new ApiMsg(200,"success");
                }else {
                    msg = new ApiMsg(200,"fail");
                }
                //写入岛的关注人数
                msg.setData(MapUtils.getMap("markCount",service.markCount(iid)));
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 当前用户取消关注岛
     */
    @ContentType
    public void delMark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String id = request.getParameter("islandId");
        ApiMsg msg;
        //判断是否登录状态
        if (user != null) {
            //判空
            if (id != null) {
                long iid = Long.parseLong(id);
                boolean result = service.delMark(user.getId(), iid);
                if (result) {
                    msg = new ApiMsg(200,"success");
                }else {
                    msg = new ApiMsg(200,"fail");
                }
                //写入岛的关注人数
                msg.setData(MapUtils.getMap("markCount",service.markCount(iid)));
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }

        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 某个岛有多少人关注
     */
    @ContentType
    public void markCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("islandId");
        ApiMsg msg;
        //判空
        if (id != null) {
            //获取岛的关注数
            long count = service.markCount(Long.parseLong(id));
            msg = ApiMsg.ok(MapUtils.getMap("markCount",count));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获得关注的岛的数量，并且返回关注的岛的id（不包括自己的）
     */
    @ContentType
    public void markIslandsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            //获取除了自己的岛之外的关注数量
            long markCount = service.getMarkIslandCount(user.getId());
            Map<String, Object> result = MapUtils.getMap("markCount", markCount);
            String page = request.getParameter("currentPage");
            String count = request.getParameter("size");
            if (page != null && count != null) {
                int currentPage = Integer.parseInt(page);
                int countNum = Integer.parseInt(count);
                //取出关注的帖子，分页
                List<Island> markIslandIds = service.getMarkIslandIds(user.getId(), currentPage, countNum);
                LOGGER.info("根据第"+currentPage+"页，取出"+count+"条记录，实际取出"+markIslandIds.size()+"条记录");
                //把获取出来的岛id放入集合
                result.put("islands",markIslandIds);
                msg = ApiMsg.ok();
            }else {
                //缺少分页参数
                List<Island> markIslandIds = service.getMarkIslandIds(user.getId());
                LOGGER.info(user.getId()+"请求时，缺少分页参数，返回全部...\t共"+markIslandIds.size()+"条记录");
                msg = new ApiMsg(200,"all");
                //把获取出来的岛id放入集合
                result.put("islands", markIslandIds);
            }
            msg.setData(result);
        }else {
            //未登录的消息
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void getHasBeen(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String size = request.getParameter("size");
            //必须指定个数
            if (size != null) {
                List<Island> hasBeen = service.getHasBeen(user.getId(), Integer.parseInt(size));
                msg = ApiMsg.ok(MapUtils.getMap("islands", hasBeen));
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void beenCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            long count = service.beenCount(user.getId());
            msg = ApiMsg.ok(MapUtils.getMap("count",count));
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取随机音频
     */
    @ContentType
    public void getVoiceRandom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String islandId = request.getParameter("belong");
        String size = request.getParameter("size");
        ApiMsg msg;
        //id和size不能为空
        if (islandId != null && size != null) {
            //获取出随机的音频
            List<IslandVoice> voiceRandom = voiceService.getVoiceRandom(Long.parseLong(islandId), Integer.parseInt(size));
            //若存在，则把音频路径设置为绝对路径
            if (voiceRandom != null && voiceRandom.size() > 0) {
                StringBuilder path = new StringBuilder(PATH).append(request.getContextPath());
                int baseLength = path.length();
                for (IslandVoice voice : voiceRandom) {
                    path.append(voice.getVoiceUrl());
                    voice.setVoiceUrl(path.toString());
                    //清除掉文件相对于项目的路径字符串
                    path.delete(baseLength, path.length());
                }
            }
            msg = ApiMsg.ok(MapUtils.getMap("voices", voiceRandom));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
}
