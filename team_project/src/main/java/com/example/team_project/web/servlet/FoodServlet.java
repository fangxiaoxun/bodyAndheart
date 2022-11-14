package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.Food;
import com.example.team_project.pojo.FoodGroup;
import com.example.team_project.service.FoodService;
import com.example.team_project.service.impl.FoodServiceImpl;
import com.example.team_project.utils.ServiceConfUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/food/*")
public class FoodServlet extends BaseServlet{
    private FoodService service = new FoodServiceImpl();

    /**
     * 返回所有的食物分组
     */
    @ContentType
    public void allGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<FoodGroup> foodGroups = service.allGroup();
        StringBuilder url = new StringBuilder(ServiceConfUtils.getConfig("HOST")).append(request.getContextPath());
        int index = url.length();
        //设置图片路径
        for (FoodGroup foodGroup : foodGroups) {
            url.append(foodGroup.getImg());
            foodGroup.setImg(url.toString());
            url.delete(index, url.length());
        }
        ApiMsg msg = ApiMsg.ok(MapUtils.getMap("groups",foodGroups));
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 通过foodId获取一条食物的详细数据
     */
    @ContentType
    public void foodInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String foodId = request.getParameter("foodId");
        ApiMsg msg;
        //判断参数是否为空
        if (foodId != null && !foodId.trim().equals("")) {
            //获取该食物id所代表的食物信息
            Food food = service.getFoodById(Integer.parseInt(foodId));
            if (food != null) {
                msg = ApiMsg.ok(MapUtils.getMap("food", food));
            }else {
                //food为null，即没有找到，也可能为参数错误，但参数错误也是找不到，故统一处理
                msg = new ApiMsg(200,"notFound");
            }
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 通过传入的小类id来获取属于这个小类的食物的信息
     * <p>其中的设置图片地址因未有数据，故未测试</p>
     */
    @ContentType
    public void getFoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("detailedId");
        ApiMsg msg = null;
        //判断参数是否为空
        if (id != null && !id.trim().equals("")) {
            //找小类中所有的食物数据
            List<Food> foodList = service.getFoodsByDetailedId(Integer.parseInt(id));
            //若List为null，则为参数错误
            if (foodList != null) {
                setUrl(foodList,request.getContextPath());
                msg = ApiMsg.ok(MapUtils.getMap("foods", foodList));
            }
        }
        //参数错误
        if (msg == null) {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 对传入的groupId对应的大类中的食物进行排序，排序的字段根据传入的参数而定，也能够设置升序排列还是降序排列
     */
    @ContentType
    public void orderedFoods(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String energy = request.getParameter("energy");
        String cho = request.getParameter("cho");
        String dietaryFiber = request.getParameter("dietaryFiber");
        String edible = request.getParameter("edible");
        String protein = request.getParameter("protein");
        String groupId = request.getParameter("groupId");
        String fat = request.getParameter("fat");
        String reverse = request.getParameter("reverse");
        ApiMsg msg;
        if (groupId != null && !groupId.trim().equals("")) {
            Set<String> orderBy = new HashSet<>(7);
            //判断存在哪些参数，有的参数就要参与排序
            if (edible != null) {
                orderBy.add("edible");
            }
            if (cho != null) {
                orderBy.add("cho");
            }
            if (energy != null) {
                orderBy.add("energy");
            }
            if (dietaryFiber != null) {
                orderBy.add("dietaryFiber");
            }
            if (protein != null) {
                orderBy.add("protein");
            }
            if (fat != null) {
                orderBy.add("fat");
            }
            List<Food> foodList;
            //若有reverse属性，则根据它来判断排序方法
            if (reverse != null) {
                foodList = service.orderedFoodList(orderBy, Integer.parseInt(groupId), Boolean.parseBoolean(reverse));
            }else {
                //若无，则默认升序
                foodList = service.orderedFoodList(orderBy, Integer.parseInt(groupId));
            }
            if (foodList.size() != 0) {
                msg = ApiMsg.ok(MapUtils.getMap("foods",foodList));
            } else {
                msg = ApiMsg.error();
            }
        } else {
            msg = new ApiMsg(200, "groupId is null");
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    @ContentType
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("kw");
        LOGGER.debug("keyword = " + keyword);
        ApiMsg msg;
        //搜索的关键词不能为空
        if (keyword != null) {
            //获取符合条件的食物数据
            List<Map<String, Object>> foodMapList = service.simpleFoodSearch(keyword);
            String contextPath = request.getContextPath();
            String host = ServiceConfUtils.getConfig("HOST");
            //设置食物的图片路径
            for (Map<String, Object> map : foodMapList) {
                String iconUrl = (String) map.get("iconUrl");
                map.put("iconUrl", host + contextPath + iconUrl);
            }
            LOGGER.debug("通过\""+keyword+"\"获取到了"+foodMapList.size()+"条食物信息");
            msg = ApiMsg.ok(MapUtils.getMap("foods", foodMapList));
        }else {
            msg = new ApiMsg(200,"参数kw缺失");
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }

    /**
     * 直接获取第一个小类的数据
     */
    @ContentType
    public void getOneType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Food> foods = service.getFoodsByDetailedId(1);
        setUrl(foods,request.getContextPath());
        mapper.writeValue(response.getOutputStream(), ApiMsg.ok(MapUtils.getMap("foods",foods)));
    }

    /**
     * 设置图片路径
     */
    private void setUrl(List<Food> foods, String contextPath) {
        StringBuilder url = new StringBuilder(ServiceConfUtils.getConfig("HOST"));
        url.append(contextPath);
        int length = url.length();
        //设置食物的图片路径
        for (Food food : foods) {
            String iconUrl = food.getIconUrl();
            if (iconUrl != null) {
                url.append(iconUrl);
                //设置图片地址
                food.setIconUrl(url.toString());
                //删除到剩下配置的路径+项目路径
                url.delete(length, url.length());
            }
        }
    }
}
