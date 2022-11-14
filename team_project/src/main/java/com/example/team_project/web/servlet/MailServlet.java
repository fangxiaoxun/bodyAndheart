package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Mail;
import com.example.team_project.pojo.MailReply;
import com.example.team_project.pojo.User;
import com.example.team_project.service.MailService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/mail/*")
public class MailServlet extends BaseServlet{
    private final MailService mailService = BeanFactory.getBean("mailService", MailService.class);

    @ContentType
    public void randomMails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String count = request.getParameter("count");
        ApiMsg msg;
        if (user != null && count != null) {
            //若有登录则调用有用户的逻辑
            List<Mail> mailList = mailService.randomMails(Integer.parseInt(count), user.getId());
            msg = ApiMsg.ok(MapUtils.getMap("mails",mailList));
        }else if (user == null && count != null){
            //若无登录，则调用无用户的逻辑，并且提醒前端未登录，可能能用于限制回复和发内容
            List<Mail> mailList = mailService.randomMails(Integer.parseInt(count), null);
            msg = new ApiMsg(200,"notLogin");
            msg.setData(MapUtils.getMap("mails",mailList));
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
    @ContentType
    public void getReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            Mail mail = mailService.getMailWithReply(user.getId());
            msg = ApiMsg.ok(MapUtils.getMap("mail",mail));
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
    @ContentType
    public void writeMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String body = request.getParameter("body");
            if (body != null) {
                boolean result = mailService.writeMail(user.getId(), body);
                if (result) {
                    msg = new ApiMsg(200,"success");
                } else {
                    msg = new ApiMsg(200,"fail");
                }
                //可以考虑给点返回值，不过现在不清楚可以给什么
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
    @ContentType
    public void reply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String mid = request.getParameter("mid");
            String content = request.getParameter("content");
            if (mid != null && content != null) {
                long mailId = Long.parseLong(mid);
                //回复后返回回复的唯一标识
                String uuid = mailService.replyMail(mailId, user.getId(), content);
                if (uuid != null) {
                    msg = new ApiMsg(200,"success");
                    //封装本次回复的信息并返回给前端
                    MailReply reply = new MailReply(mailId, user.getId(), content, uuid);
                    msg.setData(MapUtils.getMap("reply",reply));
                }else {
                    msg = new ApiMsg(200,"fail");
                }
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
    @ContentType
    public void historyMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String page = request.getParameter("currentPage");
            String size = request.getParameter("size");
            if (page != null && size != null) {
                //获取不包含评论的信以及信的总条数，可用于分页
                Map<String, Object> mailsNoReply = mailService.historyMails(user.getId(), Integer.parseInt(page), Integer.parseInt(size));
                msg = ApiMsg.ok(mailsNoReply);
            }else {
                //无分页参数
                //获取附带评论的历史信件(全部)
                List<Mail> mailList = mailService.historyMails(user.getId());
                msg = ApiMsg.ok(MapUtils.getMap("mails", mailList));
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }
    @ContentType
    public void getMailsReplies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ApiMsg msg;
        if (user != null) {
            String mid = request.getParameter("mid");
            if (mid != null) {
                String page = request.getParameter("currentPage");
                String size = request.getParameter("size");
                long id = Long.parseLong(mid);
                if (page != null && size != null) {
                    //没有分页参数
                    //获取单封信，附带回复，和回复的总数
                    Map<String, Object> mail = mailService.historyMail(id, Integer.parseInt(page), Integer.parseInt(size));
                    msg = ApiMsg.ok(mail);
                } else {
                    Mail mail = mailService.historyMail(id);
                    msg = ApiMsg.ok(MapUtils.getMap("mail", mail));
                }
            }else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.notLogin();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

}
