package com.ppan.chatapp.server;

import com.mongodb.BasicDBObject;
import com.ppan.chatapp.model.User;
import com.ppan.chatapp.utils.Constants;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/webapp-server")
public class ChatAppServer {
    private final ChatAppSessionHandler sessionHandler = new ChatAppSessionHandler();

    @OnOpen
    public void open(Session session) {
        Logger.getLogger(ChatAppServer.class.getName()).log(Level.INFO, session.getRequestURI().toString());
        Logger.getLogger(ChatAppServer.class.getName()).log(Level.INFO, session.getId() + " joined!");
        String username = session.getRequestParameterMap().get("username").get(0);
        if (username == null) {
            sendMsg(session, "username is required");
            closeSession(session);
        } else {
            if (!sessionHandler.usernameExists(username)) {
                String sessionId = session.getId();
                sessionHandler.addSession(session);
                sessionHandler.addUser(sessionId, new User(sessionId, username));
            } else {
                sendMsg(session, "username already exists");
                closeSession(session);
            }
        }
    }

    @OnClose
    public void close(Session session) {
        Logger.getLogger(ChatAppServer.class.getName()).log(Level.INFO, session.getId() + " left!");
        sessionHandler.removeSession(session);
        sessionHandler.removeUser(session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ChatAppServer.class.getName())
                .log(Level.SEVERE, "Exception occurred", error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        String serializedMsg = new BasicDBObject(Constants.SENDER_KEY, sessionHandler.getUsernameForId(session.getId()))
                .append(Constants.MSG_KEY, message).toString();
        Logger.getLogger(ChatAppServer.class.getName()).log(Level.INFO, String.format("%s says \"%s\"", session.getId(), serializedMsg));
        broadcastMsgToAll(serializedMsg);
    }

    private void broadcastMsgToAll(String message) {
        sessionHandler.sendMsgToAll(message);
    }

    public static void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            Logger.getLogger(ChatAppServer.class.getName())
                    .log(Level.SEVERE, "Exception while sending message");
        }
    }

    public void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            Logger.getLogger(ChatAppServer.class.getName())
                    .log(Level.SEVERE, "Exception while closing session. sessionId: {}", session.getId());
        }
    }
}