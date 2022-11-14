package com.example.websocket.utils;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.pojo.User;
import com.example.websocket.pojo.vo.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageUtils {
    private static final ObjectMapper mapper = BeanFactory.getBean("mapper",ObjectMapper.class);

    /**
     * 构造系统广播消息
     * @param roomNum 房间号
     * @param body 广播内容
     * @return 广播消息
     */
    public static String broadcastMessage(String roomNum, String body) {
        Message message = new Message(true, null, roomNum, body);
        String result = "{}";
        try {
            result = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 构造聊天消息
     * @param fromWhom 来自哪个用户
     * @param roomNum 房间号
     * @param body 聊天内容
     * @return 聊天信息
     */
    public static Message chatMessage(User fromWhom, String roomNum, String body) {
        return new Message(false, fromWhom, roomNum, body);
    }

    /**
     * 用户数据有误导致登录验证失败消息
     * @param errorMessage 登录失败的消息
     * @return 验证失败消息
     */
    public static String errorMessage(String errorMessage) {
        Message message = new Message(true, null, null, errorMessage);
        String result = "{}";
        try {
            result = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static<T> String message2Json(T message) {
        String content = "{}";
        try {
             content = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 把json串反序列化为对象
     * @param jsonStr 要反序列化的字符串
     * @param target 目标类
     * @param <T> 使用泛型, 使得方法更加通用
     * @return json反序列化后的对象
     */
    public static<T> T Json2Object(String jsonStr, Class<T> target) {
        T obj = null;
        if (jsonStr != null) {
            try {
                obj = mapper.readValue(jsonStr, target);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

}
