package com.ppan.chatapp.server;

import com.ppan.chatapp.utils.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "chatroom", value = "/chatroom")
public class ChatRoomServlet extends HttpServlet {

    private static final Random random = new Random();
    private static final Map<String, ChatRoom> chatrooms = Collections.synchronizedMap(new HashMap<>());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        switch (req.getRequestURI()) {
            case "/create": {
                String creator = req.getParameter(Constants.ChatRoom.CREATOR_KEY);

                String key;
                synchronized (chatrooms) {
                    key = generateRoomKey();
                    chatrooms.put(key, new ChatRoom(key, creator));
                }

                resp.getWriter().print(key);
                break;
            }
            case "/close": {
                String key = req.getParameter(Constants.ChatRoom.ROOM_KEY);

                ChatRoom room = chatrooms.remove(key);
                room.closeAll();
                break;
            }
            default: {
                resp.getWriter().print(String.format("URI %s doesn't exist", req.getRequestURI()));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getRequestURI().equals("/health")) {
            String key = req.getParameter(Constants.ChatRoom.ROOM_KEY);

            int[] countOfUsers = new int[]{-1};
            chatrooms.computeIfPresent(key, (k, chatroom) -> {
                countOfUsers[0] = chatroom.getUserCount();
                return chatroom;
            });

            if (countOfUsers[0] == -1) {
                resp.getWriter().printf("Chatroom %s doesn't exist", key);
            } else {
                resp.getWriter().printf("Chatroom %s exists with %d connected users", key, countOfUsers[0]);
            }
        } else {
            resp.getWriter().print(String.format("URI %s doesn't exist", req.getRequestURI()));
        }
    }

    private String generateRandomKey() {
        StringBuilder sb = new StringBuilder();
        String characters = Constants.ChatRoom.CHARS_FOR_KEY;

        for (int i = 0; i < Constants.ChatRoom.KEY_SIZE; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    private String generateRoomKey() {
        String key = generateRandomKey();

        while (chatrooms.containsKey(key)) {
            key = generateRandomKey();
        }

        return key;
    }

    public static ChatRoom getHandlerForRoom(String roomKey) {
        return chatrooms.get(roomKey);
    }
}
