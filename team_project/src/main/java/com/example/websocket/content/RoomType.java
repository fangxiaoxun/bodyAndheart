package com.example.websocket.content;

/**
 * 聊天室类型
 */
public enum RoomType {
    /**
     * 运动模块的聊天室
     */
    SPORT_ROOM(1),
    /**
     * 聊天模块的聊天室
     */
    CHAT_ROOM(2);

    private RoomType(int type) {
        this.type = type;
    }

    private int type;

    /**
     * 通过type值获取对应的枚举对象
     * @return type值与输入相同的枚举对象，若不存在则为null
     */
    public static RoomType valueOf(int type) {
        RoomType[] values = RoomType.values();
        for (RoomType value : values) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }
}
