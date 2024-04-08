package com.ppan.chatapp.server;

import com.ppan.chatapp.utils.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet(name = "chatroom", value = "/chatroom")
public class ChatRoomServlet extends HttpServlet {

    private static final Random random = new Random();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String creator = req.getParameter(Constants.ChatRoom.CREATOR_KEY);
        String key = generateRoomKey();

        // TODO: 09/04/24 create a chatapp server with given key

        resp.getWriter().print(key);
    }


    // TODO: 09/04/24 A room with generated key shouldn't already be present
    private String generateRoomKey() {
        StringBuilder sb = new StringBuilder();
        String characters = Constants.ChatRoom.CHARS_FOR_KEY;

        for (int i = 0; i < Constants.ChatRoom.KEY_SIZE; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
