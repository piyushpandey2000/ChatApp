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
    private ChatRoom handler;

    @OnOpen
    public void open(Session session) {
        String username = session.getRequestParameterMap().get("username").get(0);
        String roomKey = session.getRequestParameterMap().get("roomKey").get(0);

        if (username == null || roomKey == null) {
            sendErrorMsg(session, "Username and roomKey are required");
            closeSession(session);
        } else {
            handler = ChatRoomServlet.getHandlerForRoom(roomKey);
            if (handler == null) {
                sendErrorMsg(session, String.format("Room with key %s doesn't exist!", roomKey));
                closeSession(session);
            } else if (handler.usernameExists(username)) {
                sendErrorMsg(session, String.format("Username %s already exists!", username));
                closeSession(session);
            } else {
                String sessionId = session.getId();
                broadcastMsgToAll(username, null, MessageType.JOINED);

                handler.addSession(session);
                handler.addUser(sessionId, new User(sessionId, username));
                sendOnlineUsers(session, handler.getUsernames());
            }
        }
    }

    @OnClose
    public void close(Session session) {
        if (handler != null) {
            String username = handler.getUsernameForId(session.getId());
            handler.removeSession(session);
            handler.removeUser(session.getId());
            broadcastMsgToAll(username, null, MessageType.LEFT);
        }
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ChatAppServer.class.getName())
                .log(Level.SEVERE, "Exception occurred", error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        String username = handler.getUsernameForId(session.getId());
        broadcastMsgToAll(username, message, MessageType.MESSAGE);
    }

    private void broadcastMsgToAll(String sender, String message, MessageType type) {
        BasicDBObject serializedMsg = new BasicDBObject(Constants.SENDER_KEY, sender)
                .append(Constants.MSG_TYPE_KEY, type.name());
        serializedMsg.put(Constants.MSG_KEY, message);

        handler.sendMsgToAll(serializedMsg.toString());
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

    public static void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            Logger.getLogger(ChatAppServer.class.getName())
                    .log(Level.SEVERE, "Exception while closing session. sessionId: {}", session.getId());
        }
    }
}