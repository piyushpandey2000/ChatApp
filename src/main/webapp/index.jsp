<%@ page import="com.ppan.chatapp.utils.Constants" %><%--
  Created by IntelliJ IDEA.
  User: piyush.pandey
  Date: 25/02/24
  Time: 4:16 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <style>
    body, html {
      margin: 0;
      padding: 0;
      height: 100%;
      background-color: #343541;
    }
    .container {
      display: flex;
      flex-direction: row;
      height: 100vh;
    }
    .sidebar {
      flex: 1;
    }
    .main {
      flex: 3;
      border: 1px solid rgb(86, 86, 86);
      border-top: 0px;
      border-bottom: 0px;
    }
    .output {
      display: flex;
      flex-direction: column;
      height: 90vh;
      overflow-y: auto;
    }
    .input {
      height: 6vh;
      margin: 2vh;
      border: 1px solid rgb(86, 86, 86);
      border-radius: 0.5rem;
      display: flex;
      flex-direction: row;
    }
    .input-text {
      margin: 1vh;
      flex: 9;
      background-color: #343541;
      border: 0;
      color: white;
      resize: none;
    }
    .input-text:focus {
      outline: none;
    }
    .input-button {
      background-color: #343541;
      margin: 1vh;
      flex: 1;
      border: 1px solid rgb(86, 86, 86);
      color: white;
      border-radius: 0.5rem;
    }
    .msg {
      max-width: 70%;
      border-radius: 0.2rem;
      padding: 7px;
      margin: 4px 15px 4px 15px;
      align-self: flex-start;
      color: rgba(255, 255, 255, 0.655);
      font: 0.9em sans-serif;
    }
    .msg-my {
      align-self: flex-end;
      background-color: #02634b;
    }
    .msg-others {
      background-color: #484848;
    }
    .join-page, .join {
      color: white;
      font-family: "Roboto", sans-serif;
      display: flex;
      flex-direction: column;
      align-items: center; /* Vertical centering */
      justify-content: center; /* Horizontal centering */
      height: 100vh;
    }
    button {
      background-color: #343541;
      margin: 1vh;
      padding: 1vh 4vh 1vh 4vh;
      border: 1px solid rgb(86, 86, 86);
      color: white;
      border-radius: 0.5rem;
    }
    button:hover {
      background-color: #02634b;
    }
    input {
      margin: 1vh;
      padding: 1vh 4vh 1vh 4vh;
      background-color: #343541;
      border: 1px solid rgb(86, 86, 86);
      color: white;
      resize: none;
      font-family: "Roboto", sans-serif;
      text-align: center;
    }
    input:focus {
      outline: none;
    }
  </style>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chat Application</title>
</head>
<script>
  let socket;
  let username;

  function joinSession() {
    const uname = document.getElementById("username-input").value;
    console.log("username at join: " + uname);
    username = uname;
    initSocket(uname);
  }

  function initSocket(uname) {
    const outputDiv = document.getElementById("output");
    outputDiv.innerHTML = "";
    socket = new WebSocket("ws://localhost:8080/chatapp_war_exploded/webapp-server?username=" + uname)

    socket.onmessage = (event) => {
      const msgObj = JSON.parse(event.data);
      const sender = msgObj["<%= Constants.SENDER_KEY %>"];
      const msg = msgObj["<%= Constants.MSG_KEY %>"];

      const newDiv = document.createElement("div");
      newDiv.className = "msg" + sender === username ? "msg-my" : "msg-others";
      newDiv.innerHTML = "<b>" + sender + "</b><br>" + msg;

      outputDiv.appendChild(newDiv);
    };

    socket.onerror = (event) => {
      outputDiv.append("Error occurred !!!! " + event.data);
    };

    socket.onclose = () => {
      outputDiv.append("Server connection closed");
    };
  }

  function sendMessage() {
    const message = document.getElementById("input-text").value;
    console.log("message: " + message);
    socket.send(message);
  }
</script>
<body>
<div class="container">
  <div class="sidebar">
    <div class="join-page">
      <div class="join">
        Enter a username <br>
        <input id="username-input" type="text"> <br>
        <button onclick="joinSession()">Join</button>
      </div>
    </div>
  </div>
  <div class="main">
    <div class="output" id="output">
      <div class="msg msg-my">This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-my">This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-my">This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-my">This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-my">This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind This message was written by me pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
      <div class="msg msg-others">This message was written by others pls bear in mind This message was written by others pls bear in mind This message was written by others pls bear in mind</div>
    </div>
    <div class="input">
                <textarea class="input-text" id="input-text" placeholder="Type a message...">
                </textarea>
      <button class="input-button" id="send-button" onclick="sendMessage()">Send</button>
    </div>
  </div>
  <div class="sidebar"></div>
</div>
</body>
</html>