package com.ppan.chatapp.server;

import com.ppan.chatapp.model.User;
import jakarta.websocket.Session;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatAppSessionHandler {
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, User> userSessionMap = Collections.synchronizedMap(new HashMap<>());
    @Getter
    private final Set<String> usernames = Collections.synchronizedSet(new HashSet<>());

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
        sessions.forEach(session -> ChatAppServer.sendMsg(session, msg));
    }
}
