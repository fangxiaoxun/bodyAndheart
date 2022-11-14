package com.example.team_project.web.filter;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.pojo.User;
import com.example.team_project.service.UserService;
import com.example.team_project.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.util.Base64;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现自动登录
 */
@WebFilter(value = "/*")
public class LoginFilter implements Filter {
    private static final String USER_COOKIE = "userCookie";

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (request.getRequestURI().contains("login")) {//如果访问login,直接放行
            chain.doFilter(request, response);
            return;
        } else {
            Object obj = request.getSession().getAttribute("user");
            //session中没有，去Cookie中找
            if (obj == null) {
                Cookie userCookie = null;
                Cookie[] cookies = request.getCookies();
                //cookies也可能为null
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(USER_COOKIE)) {
                            userCookie = cookie;
                            break;
                        }
                    }
                }
                if (userCookie != null && userCookie.getValue() != null && !userCookie.getValue().equals("null")) {
                    UserService service = BeanFactory.getBean("userService", UserService.class);
                    ObjectMapper mapper = BeanFactory.getBean("mapper", ObjectMapper.class);
                    //对cookie中数据进行反序列化
                    User user = mapper.readValue(new String(Base64.getDecoder().decode(userCookie.getValue())), User.class);
                    user = service.loginByCookie(user.getId(),user.getPassword());
                    if (user != null) {
                        request.getSession().setAttribute("user",user);
                    }
                }
            } else {
                //session中有，放行
                chain.doFilter(request, response);
                return;
            }
        }
        //若都没有，也放行
        chain.doFilter(request,response);
    }
}
