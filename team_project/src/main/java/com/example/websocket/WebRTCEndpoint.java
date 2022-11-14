package com.example.websocket;

import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.pojo.User;
import com.example.team_project.service.UserService;
import com.example.websocket.config.GetHttpSessionEndpointConfig;
import com.example.websocket.pojo.vo.Signalling;
import com.example.websocket.pojo.vo.WebRTCType;
import com.example.websocket.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/webrtc/{roomNum}/{uid}/{password}", configurator = GetHttpSessionEndpointConfig.class)
public class WebRTCEndpoint {
    /**
     * 以下是一般的响应内容
     */
    private static final String MID_SET_FAIL_DUP = "{\"success\":false,\"code\":-1,\"msg\":\"登录失败，帐号密码错误或该id已经登录。\"}";

    private static final Map<String, Map<Long, WebRTCEndpoint>> CLIENT = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(WebRTCEndpoint.class);

    private Session session;
    private User user;
    private String roomNum;

    @OnOpen
    public void onOpen(Session session, @PathParam("roomNum") String roomNum,
                       @PathParam("uid") long uid, @PathParam("password") String password) {
        this.session = session;
        //收发文字的最大长度。默认的8192比较小，可能会不够存储较长的SDP
        session.setMaxTextMessageBufferSize(65536);
        this.roomNum = roomNum;

        UserService service = BeanFactory.getBean("userService", UserService.class);
        //用传进来的数据进行登录
        User login = service.loginBySH1Password(uid, password);
        //进行登录成功和失败的逻辑
        if (login != null) {
            user = login;
        } else {
            //发送登录失败的消息
            try {
                session.getBasicRemote().sendText(MID_SET_FAIL_DUP);
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("uid:" + uid + " 与密码:" + password + " 不匹配!");
            return;
        }
        Map<Long, WebRTCEndpoint> room = CLIENT.get(roomNum);
        if (room == null) {
            //若房间不存在，则创建一个房间，并且把房间放入存放会话的map集合中
            room = new ConcurrentHashMap<>();
            CLIENT.put(roomNum, room);
            LOGGER.debug("新建房间" + roomNum + " 成功!");
        }
        //获取连接，若为空，则添加进room，如果不为空，则发出提示
        WebRTCEndpoint conn = room.get(user.getId());
        if (conn != null) {
            LOGGER.info("用户:" + user.getId() + " 已经在房间" + roomNum + " 登录，连接失败!");
            try {
                this.user = null;
                session.getBasicRemote().sendText(MID_SET_FAIL_DUP);
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        room.put(user.getId(), this);
        //到此连接成功，发送成功的消息
        LOGGER.info("用户:" + user.getId() + " 连接WebRTC成功!");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Signalling signalling = loadSlighting(message);
        if (signalling != null) {
            String cmd = signalling.getCmd();
            try {
                if (WebRTCType.JOIN.equals(cmd)) {
                    //获取房间
                    Map<Long, WebRTCEndpoint> room = CLIENT.get(signalling.getRoomId());
                    //处理加入消息
                    handleJoin(room, user.getId());
                } else {
                    //处理与发送消息
                    handleMessage(message);
                }
            } catch (IOException e) {
                LOGGER.error("处理 " + cmd + " 时发生异常", e);
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Map<Long, WebRTCEndpoint> room = CLIENT.get(roomNum);
        if (room != null) {
            //必须得用户不为null，为null可能是登录失败的情况
            if (user != null) {
                Long uid = user.getId();
                room.remove(uid);
                LOGGER.info("用户:" + uid + " 退出房间!");
                if (room.size() == 0) {
                    CLIENT.remove(roomNum);
                    LOGGER.info("当前房间人数为0，房间关闭");
                } else {
                    String peerLeaveMessage = MessageUtils.message2Json(WebRTCType.peerLeave(uid));
                    //给还在房间中的每个用户发送退出消息
                    try {
                        for (Map.Entry<Long, WebRTCEndpoint> entry : room.entrySet()) {
                            entry.getValue().session.getBasicRemote().sendText(peerLeaveMessage);
                        LOGGER.info(uid + " send " + "peer-leave to " + entry.getKey());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        try {
            session.close();
            LOGGER.warn("遇到异常情况，已经把session关闭", e);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        e.printStackTrace();
    }

    /**
     * 用来处理发送offer，answer或candidate信令的逻辑
     *
     * @param jsonMessage json转为的字符串消息(也为前端发送过来的message),用于转成对象
     */
    private void handleMessage(String jsonMessage) throws IOException {
        //加载json，转为Signalling类
        Signalling message = loadSlighting(jsonMessage);
        //若加载失败，则为null
        if (message != null) {
            String cmd = message.getCmd();
            //开始转发信令
//            LOGGER.info("Start sending " + cmd + " signalling!");
            String roomNum = message.getRoomId();
            Map<Long, WebRTCEndpoint> room = CLIENT.get(roomNum);
            //房间不存在的情况
            if (room == null) {
                LOGGER.warn("roomNum: " + roomNum + " not exists, send " + cmd + " disable");
                return;
            }
            Long uid = message.getUid();
            //转发的信令中没有发送者的信息
            if (uid == null) {
                LOGGER.warn("uid: " + uid + " not exists, send" + cmd + " disable");
                return;
            }
            //远程用户的连接对象，RemoteUid是当前对象要发offer，answer或candidate信令的用户的id
            Long remoteUid = message.getRemoteUid();
            if (remoteUid == null) {
                LOGGER.error(uid + " will send to null");
                return;
            }
            LOGGER.info(uid + " is sending " + cmd + " to " + remoteUid);
            WebRTCEndpoint conn = room.get(remoteUid);
            if (conn != null) {
                //由测试发现，该对象可能会并发被调用
                synchronized (conn.session) {
                    conn.session.getBasicRemote().sendText(jsonMessage);
                }
                LOGGER.info(uid + " send " + cmd + " to " + remoteUid + " success!");
            } else {
                LOGGER.info("remoteUid: " + remoteUid + " not exists, send" + cmd + " disable");
            }
        }
    }

    /**
     * 处理加入的操作
     * @param room 房间内的信息
     * @param uid  新加入的用户的uid
     */
    private void handleJoin(Map<Long, WebRTCEndpoint> room, long uid) throws IOException {
        //仅有房间内人数大于1的时候，才用提醒房间里的人哪个用户进来，并且提醒刚进来的用户房间里有谁
        if (room.size() > 1) {
            List<Long> uidList = new ArrayList<>(room.size());
            for (Map.Entry<Long, WebRTCEndpoint> entry : room.entrySet()) {
                Long remoteUid = entry.getKey();
                if (!remoteUid.equals(uid)) {
                    //给房间里的用户发送newPeer消息
                    WebRTCType newPeer = WebRTCType.newPeer(uid);
                    entry.getValue().session.getBasicRemote().sendText(MessageUtils.message2Json(newPeer));
                    LOGGER.debug("send new_peer to " + remoteUid);
                    //将房间内的信息全部发出
                    uidList.add(remoteUid);
                }
            }
            //给新加入的用户发送resp-join消息
            WebRTCType respJoin = WebRTCType.respJoinType(uidList);
            session.getBasicRemote().sendText(MessageUtils.message2Json(respJoin));
            LOGGER.debug("send resp-join to " + uid);
        }
    }

    /**
     * 把jsonMessage加载为Signalling类
     *
     * @param message jsonMsg
     * @return Signalling对象，包装了信令的信息
     */
    private Signalling loadSlighting(String message) {
        ObjectMapper mapper = BeanFactory.getBean("mapper", ObjectMapper.class);
        Signalling signalling = null;
        try {
            //解析json，包装为signalling
            signalling = mapper.readValue(message, Signalling.class);
        } catch (Exception e) {
            LOGGER.error("json转为对象失败:" + message, e);
        }
        return signalling;
    }

}
