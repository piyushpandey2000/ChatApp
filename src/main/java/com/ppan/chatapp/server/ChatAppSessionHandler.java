package com.ppan.chatapp.server;

import com.ppan.chatapp.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ChatAppSessionHandler {
    private Set<Session> sessions;
    private Set<User> users;

    public ChatAppSessionHandler() {
        sessions = Collections.synchronizedSet(new HashSet<>());
        users = Collections.synchronizedSet(new HashSet<>());
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(Session session) {
        sessions.remove(session);
    }

    public void sendMsgToAll(String msg) {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                Logger.getLogger(ChatAppSessionHandler.class.getName())
                        .log(Level.SEVERE, "Exception while message broadcast");
            }
        });
    }
}
