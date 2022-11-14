package com.example.websocket.pojo.vo;

import java.util.List;
import java.util.Set;

public class WebRTCType {
    /**
     * 主动加入的信令标志
     */
    public static final String JOIN = "join";
    /**
     * 加入的响应标志，告诉加入者，房间内有什么人
     */
    public static final String RESP_JOIN = "resp-join";
    /**
     * 主动离开房间的标识
     */
    public static final String LEAVE = "leave";
    /**
     * 有新的用户加入房间
     */
    public static final String NEW_PEER = "new-peer";

    public static final String PEER_LEAVE = "peer-leave";
    /**
     * 发送的是offer信令
     */
    public static final String OFFER = "offer";
    /**
     * 发送的是answer信令
     */
    public static final String ANSWER = "answer";
    /**
     * 发送的是candidate信令
     */
    public static final String CANDIDATE = "candidate";

    private String cmd;
    private Object remoteUid;

    public WebRTCType(String cmd, Object remoteUid) {
        this.cmd = cmd;
        this.remoteUid = remoteUid;
    }

    public static WebRTCType joinType(long remoteUid) {
        return new WebRTCType(JOIN, remoteUid);
    }

    public static WebRTCType leaveType(long remoteUid) {
        return new WebRTCType(LEAVE, remoteUid);
    }

    public static WebRTCType offerType(long remoteUid) {
        return new WebRTCType(OFFER, remoteUid);
    }

    public static WebRTCType answerType(long remoteUid) {
        return new WebRTCType(ANSWER, remoteUid);
    }

    public static WebRTCType respJoinType(List<Long> remoteUid) {
        return new WebRTCType(RESP_JOIN, remoteUid);
    }

    public static WebRTCType respJoinType(Set<Long> remoteUid) {
        return new WebRTCType(RESP_JOIN, remoteUid);
    }

    public static WebRTCType newPeer(long remoteUid) {
        return new WebRTCType(NEW_PEER, remoteUid);
    }

    public static WebRTCType peerLeave(long remoteUid) {
        return new WebRTCType(PEER_LEAVE, remoteUid);

    }

    public String getCmd() {
        return cmd;
    }

    public Object getRemoteUid() {
        return remoteUid;
    }
}
