package com.ppan.chatapp.server;

import com.ppan.chatapp.model.User;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatAppSessionHandler {
    private final Set<Session> sessions;
    private final Map<String, User> userSessionMap;
    private final Set<String> usernames;

    public ChatAppSessionHandler() {
        sessions = Collections.synchronizedSet(new HashSet<>());
        userSessionMap = Collections.synchronizedMap(new HashMap<>());
        usernames = Collections.synchronizedSet(new HashSet<>());
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
        sessions.forEach(session -> ChatAppServer.sendMsg(session, msg));
    }
}
