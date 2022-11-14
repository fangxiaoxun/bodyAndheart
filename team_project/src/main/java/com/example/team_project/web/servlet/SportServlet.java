package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.SportAnswer;
import com.example.team_project.pojo.SportQuestion;
import com.example.team_project.pojo.User;
import com.example.team_project.service.SportService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/sport/*")
public class SportServlet extends BaseServlet{

    private final SportService sportService = BeanFactory.getBean("sportService", SportService.class);

    @ContentType
    public void releaseQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String title = request.getParameter("title");
            String body = request.getParameter("body");
            String qid = sportService.releaseQuestion(title, body, user.getId());
            //qid为null则是发布失败
            if (qid != null) {
                //获取刚发布的问题
                SportQuestion question = sportService.getOneQuestion(qid);
                msg = new ApiMsg(200,"success");
                msg.setData(MapUtils.getMap("question", question));
                LOGGER.debug(user.getId() + "用户的提问发布成功!");
            }else {
                msg = new ApiMsg(200, "fail");
                LOGGER.debug("参数为:title = " + title + "\nbody = " + body);
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void getQuestions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String size = request.getParameter("size");
        ApiMsg msg;
        if (size != null) {
            String page = request.getParameter("currentPage");
            String count = request.getParameter("count");
            //如果有分页参数，则执行分页操作，若没有分页参数，则回复不分页
            List<Map<String, Object>> sportQuestionsWithAnswers;
            if (page != null && count != null) {
                sportQuestionsWithAnswers = sportService.getSportQuestionsWithAnswers(
                        Integer.parseInt(size), Integer.parseInt(page), Integer.parseInt(count));
            }else {
                sportQuestionsWithAnswers = sportService.getSportQuestionsWithAnswers(Integer.parseInt(size));
            }
            LOGGER.debug("问题获取成功，要获取"+size+"条，实际获得"+sportQuestionsWithAnswers.size()+"条");
            msg = ApiMsg.ok(MapUtils.getMap("questionsWithAnswers", sportQuestionsWithAnswers));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void replyQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String qid = request.getParameter("question");
            String body = request.getParameter("body");
            if (qid != null && body != null) {
                String rid = sportService.replyQuestion(qid, body, user.getId());
                //给回复成功和失败不同的响应
                if (rid != null) {
                    msg = new ApiMsg(200, "success");
                    msg.setData(MapUtils.getMap("answer",sportService.getOneAnswer(rid)));
                    LOGGER.debug("用户:"+user.getId()+" 回复问题:" + qid + "回复成功");
                }else {
                    msg = new ApiMsg(200, "fail");
                    LOGGER.debug("用户:"+user.getId()+" 回复问题:" + qid + "回复失败\n参数为:qid = "+ qid + ";body = " + body);
                }
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 底层service和dao层未作是否为发送人，目前是人人都能删的情况
     */
    @ContentType
    public void delQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getOutputStream(), ApiMsg.error());
    }

    /**
     * 底层service和dao层未作是否为发送人，目前是人人都能删的情况
     */
    @ContentType
    public void delAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getOutputStream(), ApiMsg.error());
    }

    @ContentType
    public void answerCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("questionId");
        ApiMsg msg;
        if (id != null) {
            long count = sportService.answerCount(id);
            msg = ApiMsg.ok(MapUtils.getMap("count",count));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void like(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String aid = request.getParameter("uuid");
            SportAnswer answer = sportService.getOneAnswer(aid);
            //必须确保文章存在才能进行点赞
            if (answer != null) {
                boolean add = sportService.likeAnswer(aid, user.getId());
                long likeCount = sportService.likeCount(aid);
                if (add) {
                    msg = new ApiMsg(200, "success");
                    LOGGER.info(user.getId() + "点赞后，当前点赞数为:" + likeCount);
                } else {
                    msg = new ApiMsg(200, "fail");
                    LOGGER.info(user.getId() + "点赞失败，当前点赞数为:" + likeCount);
                }
                //返回现在点赞数
                msg.setData(MapUtils.getMap("likeCount", likeCount));
            }else {
                LOGGER.debug("文章"+aid+"不存在");
                msg = new ApiMsg(200,"answerNotFound");
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 取消点赞
     */
    @ContentType
    public void removeLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String aid = request.getParameter("uuid");
            SportAnswer answer = sportService.getOneAnswer(aid);
            //必须确保取消点赞的文章存在
            if (answer != null) {
                boolean result = sportService.removeLike(aid, user.getId());
                long likeCount = sportService.likeCount(aid);
                if (result) {
                    msg = new ApiMsg(200, "success");
                    LOGGER.info("用户"+ user.getId() + " 取消点赞成功，当前点赞数为:"+likeCount);
                }else {
                    msg = new ApiMsg(200, "fail");
                    LOGGER.info("用户"+ user.getId() + " 取消点赞失败，当前点赞数为:"+likeCount);
                }
                msg.setData(MapUtils.getMap("likeCount", likeCount));
            }else {
                msg = new ApiMsg(200,"answerNotFound");
                LOGGER.debug("文章"+aid+"不存在");
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 批量获取是否已经点赞，传进来的数组顺序与返回的顺序一致
     */
    @ContentType
    public void areLike(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String[] uuids = request.getParameterValues("uuid");
            if (uuids != null && uuids.length > 0) {
                List<Boolean> haveLike = sportService.hasLike(uuids, user.getId());
                msg = ApiMsg.ok(MapUtils.getMap("haveLike", haveLike));
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 批量获取点赞数，传入的数组顺序为返回顺序
     */
    @ContentType
    public void likeCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] aid = request.getParameterValues("uuid");
        ApiMsg msg;
        if (aid != null && aid.length > 0) {
            List<Long> likesCount = sportService.likesCount(aid);
            msg = ApiMsg.ok(MapUtils.getMap("likeCount",likesCount));
        }else {
            msg = new ApiMsg(200,"error");
            if (aid == null) {
                LOGGER.warn("没有传入参数");
            }else {
                LOGGER.warn("传入的参数个数为0");
            }
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

}
