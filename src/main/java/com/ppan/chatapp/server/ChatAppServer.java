package com.ppan.chatapp.server;

import com.mongodb.BasicDBObject;
import com.ppan.chatapp.model.User;
import com.ppan.chatapp.utils.Constants;
import com.ppan.chatapp.utils.Constants.MessageType;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/webapp-server")
public class ChatAppServer {
    @OnOpen
    public void open(Session session) {
        String username = session.getRequestParameterMap().get("username").get(0);
        if (username == null) {
            sendErrorMsg(session, "Username is required");
            closeSession(session);
        } else {
            if (!ChatAppSessionHandler.getInstance().usernameExists(username)) {
                String sessionId = session.getId();
                broadcastMsgToAll(Constants.SYSTEM_USERNAME, username + " joined", MessageType.INFO);

                ChatAppSessionHandler.getInstance().addSession(session);
                ChatAppSessionHandler.getInstance().addUser(sessionId, new User(sessionId, username));
                sendOnlineUsers(session, ChatAppSessionHandler.getInstance().getUsernames());
            } else {
                sendErrorMsg(session, "Username already exists!");
                closeSession(session);
            }
        }
    }

    @OnClose
    public void close(Session session) {
        String username = ChatAppSessionHandler.getInstance().getUsernameForId(session.getId());
        ChatAppSessionHandler.getInstance().removeSession(session);
        ChatAppSessionHandler.getInstance().removeUser(session.getId());
        broadcastMsgToAll(Constants.SYSTEM_USERNAME, username + " left", MessageType.INFO);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ChatAppServer.class.getName())
                .log(Level.SEVERE, "Exception occurred", error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        String username = ChatAppSessionHandler.getInstance().getUsernameForId(session.getId());
        broadcastMsgToAll(username, message, MessageType.MESSAGE);
    }

    private void broadcastMsgToAll(String sender, String message, MessageType type) {
        String serializedMsg = new BasicDBObject(Constants.SENDER_KEY, sender)
                .append(Constants.MSG_KEY, message)
                .append(Constants.MSG_TYPE_KEY, type.name()).toString();
        ChatAppSessionHandler.getInstance().sendMsgToAll(serializedMsg);
    }

    private void sendErrorMsg(Session session, String msg) {
        String serializedMsg = new BasicDBObject(Constants.MSG_KEY, msg)
                .append(Constants.MSG_TYPE_KEY, MessageType.ERROR.name()).toString();
        sendMsg(session, serializedMsg);
    }

    private void sendOnlineUsers(Session session, Set<String> msg) {
        String serializedMsg = new BasicDBObject(Constants.ONLINE_USERS, msg)
                .append(Constants.MSG_TYPE_KEY, MessageType.DATA.name()).toString();
        sendMsg(session, serializedMsg);
    }

    public static void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            Logger.getLogger(ChatAppServer.class.getName())
                    .log(Level.SEVERE, "Exception while sending message");
        }
    }

    private void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            Logger.getLogger(ChatAppServer.class.getName())
                    .log(Level.SEVERE, "Exception while closing session. sessionId: {}", session.getId());
        }
    }
}