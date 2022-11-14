package com.example.websocket;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.pojo.Icon;
import com.example.team_project.pojo.User;
import com.example.team_project.service.ChatRecordService;
import com.example.team_project.service.IconService;
import com.example.team_project.service.UserService;
import com.example.websocket.config.GetHttpSessionEndpointConfig;
import com.example.websocket.content.RoomType;
import com.example.websocket.pojo.vo.ChatRoom;
import com.example.websocket.pojo.vo.Message;
import com.example.websocket.utils.MessageUtils;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天室，uid和password可能能从sessionStorage获取
 */
@ServerEndpoint(value = "/chat/{roomNum}/{uid}/{password}", configurator = GetHttpSessionEndpointConfig.class)
public class ChatEndpoint {
    /**
     * 存放所有的连接信息
     * <p>key为房间信息(hashcode按照roomNum比较，故roomNum不得重复), value的key是用户id, value的value是连接对象</p>
     */
    private static final Map<ChatRoom, Map<Long, ChatEndpoint>> CLIENT = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(ChatEndpoint.class);
    /**
     * 连接数量，需要保证线程安全
     */
    private static int connCount = 0;
    private Session session;
    private User user;
    private String roomNum;

    @OnOpen
    public void onOpen(Session session, @PathParam("roomNum") String roomNum, @PathParam("uid") long uid,
                       @PathParam("password") String password) {
        this.session = session;
        this.roomNum = roomNum;
        ChatRoom condition = ChatRoom.createCondition(roomNum);
        Map<Long, ChatEndpoint> room = CLIENT.get(condition);
        //只有当房间还不存在，或用户还没加入这个房间时，才可以进行加入逻辑
        if (room == null || !room.containsKey(uid)) {
            User user = BeanFactory.getBean("userService", UserService.class).loginBySH1Password(uid, password);
            if (user != null) {
                this.user = user;
                Icon icon = BeanFactory.getBean("iconService", IconService.class).getUserIconByMark(user.getIconMark());
                user.setIconUrl(icon.getIconUrl());
            } else {
                //登录失败则发送登录消息
                String loginError = MessageUtils.errorMessage("loginError");
                LOGGER.info(uid + "登录失败");
                session.getAsyncRemote().sendText(loginError);
                //登录失败关闭session，避免内存泄露
                closeRecourse();
                return;
            }
            //加入房间
            joinRoom(roomNum);
            addConn();
        } else {
            //发出用户已经存在的消息
            String errorMessage = MessageUtils.errorMessage("您已经进入了该房间，本次加入失败!");
            sendError(errorMessage);
            //关闭本次连接的资源
            closeRecourse();
        }
        //给该用户发送历史消息
        ChatRecordService service = BeanFactory.getBean("chatRecordService", ChatRecordService.class);
        List<Message> records = service.getRecords(roomNum);
        for (Message record : records) {
            try {
                session.getBasicRemote().sendText(MessageUtils.message2Json(record));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        //获取发消息的房间
        ChatRoom condition = ChatRoom.createCondition(roomNum);
        Map<Long, ChatEndpoint> room = CLIENT.get(condition);
        if (room == null) {
            //给发消息方发送房间不存在的消息
            String errorMessage = MessageUtils.errorMessage("room not exists");
            sendError(errorMessage);
        }
        //构造聊天
        Message chat = MessageUtils.chatMessage(user, roomNum, message);
        //发送消息内容
        String chatMessage = MessageUtils.message2Json(chat);
        send2All(room, chatMessage);
        //把聊天存入数据库
        ChatRecordService chatRecordService = BeanFactory.getBean("chatRecordService", ChatRecordService.class);
        chat.setFromWhom(chat.getSender().getId());
        chatRecordService.addRecord(chat);
    }

    @OnClose
    public void onClose(Session session) {
        if (user != null) {
            Long uid = user.getId();
            String message = MessageUtils.broadcastMessage(roomNum, "用户" + uid + " 离开了聊天室");
            //获取用户所在的房间
            ChatRoom condition = ChatRoom.createCondition(roomNum);
            Map<Long, ChatEndpoint> room = CLIENT.get(condition);
            //把用户移出聊天室
            removeUser(room);
            //把离开聊天室的消息广播
            send2All(room, message);
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        LOGGER.error("", e);
        boolean open = session.isOpen();
        //检查出错的用户是否已经离开了聊天室，若是，则将其移出Map集合
        if (!open) {
            if (user != null) {
                ChatRoom condition = ChatRoom.createCondition(roomNum);
                Map<Long, ChatEndpoint> room = CLIENT.get(condition);
                if (room != null) {
                    //移除用户
                    removeUser(room);
                    LOGGER.info("已经将会话关闭的用户 " + user.getId() + " 移出聊天区");
                }
            }
        } else {
            try {
                session.getBasicRemote().sendText("出现错误");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 给房间内的所有用户统一发送消息
     *
     * @param room    统一要发消息的房间
     * @param message 要发送的消息
     */
    private void send2All(Map<Long, ChatEndpoint> room, String message) {
        if (room != null) {
            //给房间内的所有人发消息
            for (Map.Entry<Long, ChatEndpoint> entry : room.entrySet()) {
                entry.getValue().session.getAsyncRemote().sendText(message);
            }
        } else {
            //房间不存在则发送房间不存在的消息给这个申请加入的用户房间不存在的消息
            try {
                //这可能是最后一个人退出房间，当最后一个人退出房间，房间为null，且session是关闭的
                if (session.isOpen()) {
                    Message m = new Message(true, null, null, "rootNotExists");
                    session.getBasicRemote().sendText(MessageUtils.message2Json(m));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用户加入房间的相关逻辑
     * @param roomNum 加入的房间号
     */
    private void joinRoom(String roomNum) {
        ChatRoom condition = ChatRoom.createCondition(roomNum);
        Map<Long, ChatEndpoint> room = CLIENT.get(condition);
        Long uid = user.getId();
        if (room == null) {
            //获取创建房间的参数
            Map<String, List<String>> params = session.getRequestParameterMap();
            List<String> typeList = params.get("type");
            if (typeList == null) {
                String errorMessage = MessageUtils.errorMessage("未指定房间类型错误");
                LOGGER.warn(user.getId() + "创建房间时，未指定房间类型");
                sendError(errorMessage);
                user = null;
                //关闭连接
                closeRecourse();
                return;
            }else {
                //设置房间的类型
                if (typeList.size() > 0) {
                    String type = typeList.get(0);
                    //通过type获取房间类型
                    RoomType roomType = RoomType.valueOf(Integer.parseInt(type));
                    condition = new ChatRoom(roomNum, roomType);
                }
                //当类型错误时，返回错误原因，并且关闭连接
                if (condition.getType() == null) {
                    String errorMessage = MessageUtils.errorMessage("房间类型指定错误");
                    LOGGER.warn(user.getId() + "创建房间时，房间类型指定错误");
                    sendError(errorMessage);
                    user = null;
                    closeRecourse();
                    return;
                }
            }
            //房间不存在则创建房间
            List<String> descList = params.get("desc");
            String desc;
            //让主题不为空
            if (descList == null) {
                desc = "";
            } else {
                //若有参数，则使用第一个作为主题，若无则保证主题不得为空
                if (descList.size() > 0) {
                    desc = descList.get(0);
                }else {
                    desc = "";
                }
            }
            //添加主题
            condition.setDesc(desc);
            room = new ConcurrentHashMap<>();
            CLIENT.put(condition, room);
            LOGGER.debug("房间 " + roomNum + " 创建成功!");
        }
        //把用户的连接对象加入代表房间的集合中
        room.put(uid, this);
        LOGGER.info(uid + " enter room " + roomNum);
        String broadcast = MessageUtils.broadcastMessage(roomNum, uid + " 用户加入" + roomNum + "聊天室");
        for (Map.Entry<Long, ChatEndpoint> entry : room.entrySet()) {
            //给房间内每一位用户发送用户加入聊天室的消息(用异步，使得每个人收到消息的时间和效率提升)
            entry.getValue().session.getAsyncRemote().sendText(broadcast);
        }
    }

    private void closeRecourse() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeUser(Map<Long, ChatEndpoint> room) {
        if (room != null) {
            //移除退出的用户
            Long uid = user.getId();
            room.remove(uid);
            LOGGER.info("用户" + uid + " 离开了聊天室");
            //若房间中没有人，则关闭房间
            if (room.size() <= 0) {
                ChatRoom condition = ChatRoom.createCondition(roomNum);
                CLIENT.remove(condition);
                LOGGER.info("房间当前人数为0，关闭成功");
                //清空该房间的聊天记录
                ChatRecordService chatRecordService = BeanFactory.getBean("chatRecordService", ChatRecordService.class);
                chatRecordService.delRecords(roomNum);
            }
            reduceConn();
        }
    }

    private void sendError(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加连接数量
     */
    private synchronized static void addConn() {
        connCount++;
    }

    /**
     * 减少连接数量
     */
    private synchronized static void reduceConn() {
        connCount--;
    }

    /**
     * 获取连接数,读写分离，这样读的速度比较快，又能保证写的时候线程安全
     * @return 连接数
     */
    public static int getConnCount() {
        return connCount;
    }

    /**
     * 检验房间号合法性(是否存在)
     * @param roomNum 要进行检测的房间号
     * @return 合法为true，非法为false
     */
    public static boolean checkRoomNumLegitimacy(String roomNum) {
        ChatRoom room = ChatRoom.createCondition(roomNum);
        return !CLIENT.containsKey(room);
    }

    /**
     * 返回单个房间的信息
     * @param roomNum 房间号
     * @return 该房间号对应的房间信息
     */
    public static ChatRoom getDetails(String roomNum) {
        Set<ChatRoom> chatRooms = CLIENT.keySet();
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getRoomNum().equals(roomNum)) {
                return chatRoom;
            }
        }
        LOGGER.warn("cannot find room ==> num = " + roomNum);
        return null;
    }

    /**
     * 获得所有房间的信息
     */
    public static Set<ChatRoom> getDetails() {
        return CLIENT.keySet();
    }

    /**
     * 获取聊天室中属于CHAT_ROOM的房间信息
     * @return 所有的CHAT_ROOM类型的房间
     */
    public static List<Map<String, Object>> chatRooms() {
        Set<ChatRoom> details = getDetails();
        List<Map<String, Object>> roomList = new ArrayList<>();
        for (ChatRoom chatRoom : details) {
            if (RoomType.CHAT_ROOM.equals(chatRoom.getType())) {
                Map<String, Object> room = new HashMap<>(4);
                //将房间的信息和房间内在线人数封装到map集合
                room.put("room", chatRoom);
                room.put("onlineCount", roomOnlineNum(chatRoom));
                roomList.add(room);
            }
        }
        return roomList;
    }

    /**
     * 获取聊天室中属于SPORT_ROOM的房间信息
     * @return 所有的SPORT_ROOM类型的房间
     */
    public static List<Map<String, Object>> sportRooms() {
        Set<ChatRoom> details = getDetails();
        List<Map<String, Object>> roomList = new ArrayList<>();
        for (ChatRoom chatRoom : details) {
            if (RoomType.SPORT_ROOM.equals(chatRoom.getType())) {
                Map<String, Object> room = new HashMap<>();
                room.put("rooms",chatRoom);
                room.put("onlineCount",roomOnlineNum(chatRoom));
                roomList.add(room);
            }
        }
        return roomList;
    }

    public static int roomOnlineNum(ChatRoom chatRoom) {
        Map<Long, ChatEndpoint> room = CLIENT.get(chatRoom);
        return room.size();
    }
}
