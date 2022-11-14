package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Question;
import com.example.team_project.service.SecurityService;
import com.example.team_project.service.impl.SecurityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/security/*")
public class SecurityServlet extends BaseServlet {
    private SecurityService service = new SecurityServiceImpl();

    /**
     * 获取用户拥有的所有的密保问题
     */
    public void questions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("id");
        ApiMsg msg = null;
        if (user != null) {
            List<Question> questions = service.getQuestions(Long.parseLong(user));
            response.setContentType("application/json;charset=utf-8");
            Map<String, Object> value = new HashMap<>();
            value.put("question", questions);
            msg = ApiMsg.ok(value);
        }else {
            msg = ApiMsg.error("id is null");
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 判断输入的答案与设置的答案是否相同
     */
    public void compare(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("id");
        String question = request.getParameter("question");
        String answer = request.getParameter("answer");
        ApiMsg msg = ApiMsg.error();
        if (!"".equals(user) && !"".equals(question) && !"".equals(answer) &&
        user != null && question != null && answer != null) {
            long userId = Long.parseLong(user);
            int questionId = Integer.parseInt(question);
            if (userId > 0 && questionId > 0) {
                boolean compare = service.compare(userId, questionId, answer);
                msg.setMessage(Boolean.toString(compare));
            }
        }
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 添加密保问题
     * 当使用的是数据库中不存在的对象，会遇到死锁问题
     */
    public void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String user = request.getParameter("id");
        String question = request.getParameter("question");
        String answer = request.getParameter("answer");
        // 默认为出错
        ApiMsg msg = ApiMsg.error();
        // 对传入数据做判空
        if (!"".equals(user) && !"".equals(question) && !"".equals(answer) && user != null &&
                question != null && answer != null) {
            //死锁问题会发生在此
            boolean add = service.add(Long.parseLong(user), Integer.parseInt(question), answer);
            msg.setMessage("fail");
            if (add) {
                msg.setMessage("success");
            }
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 获取所有密保问题
     */
    public void all(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ApiMsg msg = ApiMsg.ok();
        List<Question> all = service.all();
        if (all.size() != 0){
            msg.setData(MapUtils.getMap("questions",all));
        }
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 所有用户没有设置的密保问题
     */
    public void notHave(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        response.setContentType("application/json;charset=utf-8");
        //默认为错误
        ApiMsg msg = ApiMsg.error();
        if (id != null && !id.equals("")) {
            long userIf = Long.parseLong(id);
            if (userIf > 0) {
                List<Question> noHave = service.findNoHave(userIf);
                msg.setMessage("OK");
                if (noHave.size() != 0) {
                    msg.setData(MapUtils.getMap("noHave", noHave));
                } else {
                    msg.setData(null);
                }
            }
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }
}
