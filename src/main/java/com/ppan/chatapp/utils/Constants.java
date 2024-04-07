package com.ppan.chatapp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String SENDER_KEY = "sender";
    public static final String MSG_KEY = "msg";
    public static final String MSG_TYPE_KEY = "mType";
    public static final String ONLINE_USERS = "onlineUsers";

    public enum MessageType {
        MESSAGE,
        ERROR,
        DATA,
        JOINED,
        LEFT
    }
}
