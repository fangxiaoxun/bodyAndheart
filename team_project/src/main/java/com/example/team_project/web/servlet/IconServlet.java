package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Icon;
import com.example.team_project.pojo.User;
import com.example.team_project.service.IconService;
import com.example.team_project.service.impl.IconServiceImpl;
import com.example.team_project.utils.ServiceConfUtils;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/icon/*")
public class IconServlet extends BaseServlet {
    private static final String HOST = ServiceConfUtils.getConfig("HOST");
    private IconService service = BeanFactory.getBean("iconService",IconService.class);

    /**
     * 提供iconMark(int)参数(通过user信息获取)，通过参数获取头像
     */
    public void getIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mark = request.getParameter("iconMark");
        response.setContentType("application/json;charset=utf-8");
        ApiMsg msg;
        if (mark != null && !mark.trim().equals("")) {
            int iconMark = Integer.parseInt(mark);
            //iconMark不能小于0
            if (iconMark > 0) {
                Icon icon = service.getUserIconByMark(iconMark);
                //获取url的路径
                String url = HOST + request.getContextPath() + icon.getIconUrl();
                icon.setIconUrl(url);
                msg = ApiMsg.ok();
                if (icon != null) {
                    msg.setData(MapUtils.getMap("icon", icon));
                }
            } else {
                msg = ApiMsg.error();
            }
        } else {
            //没有参数或参数为空字符串
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 获取所有的头像
     */
    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        //获取参数
        String page = request.getParameter("currentPage");
        String count = request.getParameter("count");
        ApiMsg msg;
        if (page != null && count != null && !page.trim().equals("") && !count.trim().equals("")){
            int current = Integer.parseInt(page);
            int countNum = Integer.parseInt(count);
            if (current > 0 && countNum > 0) {
                //分页查询，拿到一页的头像
                List<Icon> icons = service.allIcons(current, countNum);
                setIconsPath(icons,request.getContextPath());
                msg = ApiMsg.ok(MapUtils.getMap("icons", icons));
            }else {
                msg = ApiMsg.error();
            }
        } else {
            //缺少参数则不分页
            long total = service.getCount();
            List<Icon> icons = service.allIcons(1, Math.toIntExact(total));
            setIconsPath(icons,request.getContextPath());
            msg = ApiMsg.ok(MapUtils.getMap("icons",icons));
            msg.setMessage("all");
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 设置用户的头像
     */
    public void setIcon(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        response.setContentType("application/json;charset=utf-8");
        String userId = request.getParameter("id");
        String mark = request.getParameter("iconMark");
        ApiMsg msg;
        //判空
        if (mark != null && !mark.trim().equals("") && userId != null && !userId.trim().equals("")) {
            int iconMark = Integer.parseInt(mark);
            long id = Long.parseLong(userId);
            //判数据是否符合要求，减少不符合要求的数据访问数据库
            if (iconMark > 0 && id > 0) {
                boolean flag = service.setIcon(id, iconMark);
                if (flag) {
                    msg = new ApiMsg(200,"success");
                    User user = (User) request.getSession().getAttribute("user");
                    user.setIconMark(iconMark);
                } else {
                    msg = new ApiMsg(200,"fail");
                }
            } else {
                msg = ApiMsg.error();
            }
        } else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 获取头像的最大数量
     */
    public void getCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        long count = service.getCount();
        mapper.writeValue(response.getOutputStream(), ApiMsg.ok(MapUtils.getMap("count",count)));
    }

    /**
     * 设置图片的路径（设置为绝对路径）
     * @param icons icon的集合
     * @param contextPath 通过request.getContextPath()获取
     */
    public static void setIconsPath(List<Icon> icons, String contextPath) {
        StringBuilder url = new StringBuilder();
        for (Icon icon : icons) {
            url.append(HOST);
            url.append(contextPath);
            url.append(icon.getIconUrl());
            icon.setIconUrl(url.toString());
            url.delete(0,url.length());
        }
    }
}
