package com.ppan.chatapp.server;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.bson.BasicBSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint("/webapp-server")
public class ChatAppServer {
    @Inject
    private ChatAppSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ChatAppServer.class.getName())
                .log(Level.SEVERE, "Exception occurred", error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        String serializedMsg = new BasicBSONObject("sender", session.getId()).append("msg", message).toString();
        broadcastMsgToAll(serializedMsg);
    }

    private void broadcastMsgToAll(String message) {
        sessionHandler.sendMsgToAll(message);
    }
}