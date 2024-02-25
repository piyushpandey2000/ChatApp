<%--
  Created by IntelliJ IDEA.
  User: piyush.pandey
  Date: 25/02/24
  Time: 4:10 pm
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
      color: white;
      font-family: "Roboto", sans-serif;
    }
    .join-page, .join {
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
  <title>Chatapp - Join Server</title>
</head>
<body>
<div class="container">
  <div class="sidebar"></div>
  <div class="main">
    <div class="join-page">
      <div class="join">
        Enter a username <br>
        <input type="text"> <br>
        <button>Join</button>
      </div>
    </div>
  </div>
  <div class="sidebar"></div>
</div>
</body>
</html>
