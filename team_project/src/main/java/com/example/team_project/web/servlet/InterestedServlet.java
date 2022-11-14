package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Hobby;
import com.example.team_project.pojo.Interested;
import com.example.team_project.service.HobbyService;
import com.example.team_project.service.impl.HobbyServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/interest/*")
public class InterestedServlet extends BaseServlet{
    private HobbyService service = new HobbyServiceImpl();

    /**
     * 用户添加爱好
     * 需要传入用户id和从复选框中选值,会有死锁问题
     */
    public void userAdd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String id = request.getParameter("id");
        //用户的选择的爱好
        String[] hobbies = request.getParameterValues("adds");
        //默认为error
        ApiMsg msg = ApiMsg.error();
        if (id != null && hobbies != null && !id.trim().equals("") && hobbies.length != 0) {
            int[] hobbiesId = new int[hobbies.length];
            int pos = 0;
            //逐项赋值，遇到为null或空字符串的，忽略
            for (String hobby : hobbies) {
                if (hobby != null && !hobby.trim().equals("")) {
                    hobbiesId[pos] = Integer.parseInt(hobby);
                    pos++;
                }
            }
            boolean result = service.addHobby(Long.parseLong(id), hobbiesId);
            //对于修改成功和失败分别给出不同响应
            if (result){
                msg.setMessage("success");
            }else {
                msg.setMessage("fail");
            }
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 与add基本相似，会有死锁问题
     */
    public void userDel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String id = request.getParameter("id");
        String[] deletes = request.getParameterValues("deletes");
        ApiMsg msg = ApiMsg.error();
        if (id != null && deletes != null && deletes.length != 0 && !id.trim().equals("")) {
            int pos = 0;
            int[] deletesId = new int[deletes.length];
            for (String delete : deletes) {
                if (delete != null && !delete.trim().equals("")) {
                    deletesId[pos] = Integer.parseInt(delete);
                    pos++;
                }
            }
            boolean result = service.deleteHobby(Long.parseLong(id), deletesId);
            if (result) {
                msg.setMessage("success");
            }else {
                msg.setMessage("fail");
            }
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 获得所有的hobby，包括用户有的和没有的
     * <p>有参数分页，无参数不分页</p>
     */
    public void allHobbies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String page = request.getParameter("currentPage");
        String count = request.getParameter("count");
        List<Hobby> all;
        if (page != null && count != null && !page.trim().equals("") && !count.trim().equals("")) {
            int current = Integer.parseInt(page);
            int counts = Integer.parseInt(count);
            if (current > 0 && counts > 0) {
                all = service.all(current,counts);
            } else {
                all = service.all();
            }
        } else {
            all = service.all();
        }
        ApiMsg msg = ApiMsg.error();
        if (all.size() != 0) {
            msg.setMessage("OK");
            msg.setData(MapUtils.getMap("hobbies", all));
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }

    /**
     * 获取用户所拥有的hobby
     */
    public void userHas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String id = request.getParameter("id");
        String page = request.getParameter("currentPage");
        String countNum = request.getParameter("count");
        ApiMsg msg;
        if (id != null && !id.trim().equals("")) {
            long userId = Long.parseLong(id);
            if (userId > 0) {
                //获取用户爱好列表
                List<Interested> list;
                //判断是否有分页参数，有则分页，没有则不分页
                if (page != null && countNum != null && !page.trim().equals("") && !countNum.trim().equals("")){
                    int currentPage = Integer.parseInt(page);
                    int count = Integer.parseInt(countNum);
                    //判断分页参数是否符合要求，符合则分，不符合则不分
                    if (count > 0 && currentPage > 0) {
                        list = service.userHas(userId,currentPage,count);
                    } else {
                        list = service.userHas(userId);
                    }
                }else {
                    list = service.userHas(userId);
                }
                msg = ApiMsg.ok(MapUtils.getMap("interested", list));
            } else {
                msg = ApiMsg.error();
            }
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(),msg);
    }
}
