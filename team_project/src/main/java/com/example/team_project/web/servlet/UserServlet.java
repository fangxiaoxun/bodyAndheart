package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.comment.emun.ConditionType;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Controller;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.User;
import com.example.team_project.service.UserService;
import com.example.team_project.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private static final String USER_COOKIE = "userCookie";
    /**
     * 由于没有实现controller，故直接从缓存中获取UserService对象
     */
    private UserService service = BeanFactory.getBean("userService",UserService.class);

    /**
     * 需传入id，password
     * <p>登录成功返回用户信息，登录失败data为空，且msg=loginError</p>
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        //获取参数
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String autoLogin = request.getParameter("autoLogin");
        ApiMsg msg = ApiMsg.ok();
        if (id != null && password != null && !id.trim().equals("") && !password.trim().equals("")) {
            HttpSession session = request.getSession();
            long userId = Long.parseLong(id);
            User login = null;
            if (userId > 0) {
                login = service.login(userId, password);
            }
            //判断是否登录成功
            if (login == null) {
                msg.setMessage("loginError");
            } else {
                //成功则把用户存到session域
                msg.setData(MapUtils.getMap("user",login));
                session.setAttribute("user", login);
                //判断用户是否要自动登录
                if (autoLogin != null && autoLogin.equals("true")) {
                    //把用户数据保存到cookie
                    String value = mapper.writeValueAsString(login);
                    Cookie cookie = new Cookie(USER_COOKIE, Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)));
                    //设置cookie最多保存7天
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(cookie);
                } else {
                    //cookie的value不能设置为null，不然会替换失败
                    Cookie cookie = new Cookie(USER_COOKIE, "null");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        } else {
            msg.setMessage("error");
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 判断是否已经登录
     *
     * @return 未登录，msg为0，且没有数据返回。已经登录，msg为1，并返回用户数据
     */
    public void userExist(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        response.setContentType("application/json;charset=utf-8");
        ApiMsg msg;
        if (user == null) {
            //不存在则状态为0
            msg = new ApiMsg(200, "0");
        } else {
            //存在则状态为1,并返回用户数据
            msg = new ApiMsg(200, "1");
            msg.setData(MapUtils.getMap("user",user));
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 注册账号
     * @return 注册成功返回注册的账号
     */
    public void signIn(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        String password = request.getParameter("password");
        String iconMark = request.getParameter("iconMark");
        ApiMsg msg;
        //判空·
        if (password != null && iconMark != null && !password.trim().equals("") && !iconMark.trim().equals("")) {
            int mark = Integer.parseInt(iconMark);
            //判断头像是否符合要求
            if (mark > 0) {
                Long id = service.signIn(password, mark);
                msg = ApiMsg.ok(MapUtils.getMap("id", id));
                request.getSession().removeAttribute("user");
                Cookie cookie = new Cookie(USER_COOKIE,"null");
                response.addCookie(cookie);
            } else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 退出登录
     * @return 退出正常不会返回错误信息，退出异常则返回500状态码
     */
    public void logoff(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        try {
            request.getSession().removeAttribute("user");
            Cookie cookie = new Cookie(USER_COOKIE,"null");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            ApiMsg msg = (ApiMsg) request.getAttribute("msg");
            if (msg == null) {
                msg = new ApiMsg(200, "success");
            }
            mapper.writeValue(response.getOutputStream(),msg);
        } catch (Exception e) {
            ApiMsg msg = new ApiMsg(ConditionType.SERVICE_UN_AVAILABLE.getCode(), ConditionType.SERVICE_UN_AVAILABLE.getMsg());
            mapper.writeValue(response.getOutputStream(), msg);
        }
    }

    /**
     * 忘记密码（改密码）
     * <p>若修改后的密码和二次确认的密码有一个或以上不存在(或为空字符串)，则返回error信息</p>
     * <p>若修改的密码与二次确认的密码不同，则返回difference消息</p>
     * <p>若修改成功，则返回ok消息</p>
     * <p>若修改失败，则返回fail消息</p>
     */
    public void forget(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String reKeyIn = request.getParameter("reKeyIn");
        response.setContentType("application/json;charset=utf-8");
        //判空
        if (id != null && password != null && reKeyIn != null && !"".equals(password) && !"".equals(reKeyIn)) {
            password = password.trim();
            reKeyIn = reKeyIn.trim();
            if (password.equals(reKeyIn)) {
                long userId = Long.parseLong(id);
                boolean result = false;
                if (userId > 0) {
                    result = service.changePassword(userId, password);
                }
                if (result) {
                    //给转发后登出账号做标识
                    request.setAttribute("msg",ApiMsg.ok());
                    request.getRequestDispatcher("/user/logoff").forward(request,response);
                } else {
                    mapper.writeValue(response.getOutputStream(), new ApiMsg(200, "fail"));
                }
            } else {
                mapper.writeValue(response.getOutputStream(), new ApiMsg(200, "difference"));
            }
        } else {
            mapper.writeValue(response.getOutputStream(), new ApiMsg(200, "error"));
        }
    }

}
