package com.example.websocket.pojo.vo;

public class Signalling {
    private String cmd;
    private String roomId;
    private Long uid;
    private Long remoteUid;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(Long remoteUid) {
        this.remoteUid = remoteUid;
    }

    public void setMsg(String msg) {
    }

    @Override
    public String toString() {
        return "Signalling{" +
                "cmd='" + cmd + '\'' +
                ", roomId='" + roomId + '\'' +
                ", uid=" + uid +
                ", remoteUid=" + remoteUid +
                '}';
    }
}
