package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的servlet类们的父类，用于实现分发servlet，并且能做统一操作，减少代码
 */
public abstract class BaseServlet extends HttpServlet {
    protected ObjectMapper mapper = BeanFactory.getBean("mapper", ObjectMapper.class);
    protected final Logger LOGGER = Logger.getLogger(this.getClass());
    /**
     * 用于判断方法上有没有ContentType注解,这个注解用于设置ContentType
     */
    private static final Class<ContentType> CLAZZ = ContentType.class;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置这个请求头解决跨域问题
        resp.setHeader("Access-Control-Allow-Origin","*");

        String uri = req.getRequestURI();
        //获取get请求的地址
        if (uri.contains("?")) {
           uri = uri.substring(uri.lastIndexOf('/') + 1,uri.lastIndexOf('?'));
        }else {
            uri = uri.substring(uri.lastIndexOf('/') + 1);
        }
        try {
            //获取servlet方法
            Method method = this.getClass().getMethod(uri, HttpServletRequest.class, HttpServletResponse.class);
            //若存在contentType注解，则把contentType注解中的contentType中的数组取出，然后对resp设置这些contentType
            if (method.isAnnotationPresent(CLAZZ)) {
                ContentType annotation = method.getAnnotation(CLAZZ);
                String[] contentType = annotation.contentType();
                for (String type : contentType) {
                    resp.setContentType(type);
                }
            }
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException noMethod){
            resp.setContentType("application/json;charset=utf-8");
            //返回资源未找到
            mapper.writeValue(resp.getOutputStream(),ApiMsg.notFound());
        } catch (Exception e) {
            ApiMsg msg = ApiMsg.serveException();
            resp.setContentType("application/json;charset=utf-8");
            e.printStackTrace();
            //返回错误信息
            mapper.writeValue(resp.getOutputStream(),msg);
        }
    }

}
