package com.ppan.chatapp.server;

import com.ppan.chatapp.model.User;
import jakarta.websocket.Session;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatRoom {
    private final String roomKey;
    private final String creator;

    private final Set<Session> sessions;
    private final Map<String, User> userSessionMap;
    @Getter
    private final Set<String> usernames;

    public ChatRoom(String roomKey, String creator) {
        this.roomKey = roomKey;
        this.creator = creator;
        this.sessions = Collections.synchronizedSet(new HashSet<>());
        this.userSessionMap = Collections.synchronizedMap(new HashMap<>());
        this.usernames = Collections.synchronizedSet(new HashSet<>());
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void addUser(String sessionId, User user) {
        userSessionMap.put(sessionId, user);
        usernames.add(user.getUsername());
    }

    public void removeUser(String sessionId) {
        User user = userSessionMap.get(sessionId);
        if (user != null) {
            usernames.remove(user.getUsername());
            userSessionMap.remove(sessionId);
        }
    }

    public boolean usernameExists(String username) {
        return usernames.contains(username);
    }

    public String getUsernameForId(String id) {
        return userSessionMap.get(id).getUsername();
    }

    public void sendMsgToAll(String msg) {
        Session[] array = sessions.toArray(new Session[0]);
        for (Session session : array) {
            ChatAppServer.sendMsg(session, msg);
        }
    }

    public void closeAll() {
        sessions.forEach(ChatAppServer::closeSession);
    }

    public int getUserCount() {
        return usernames.size();
    }
}
