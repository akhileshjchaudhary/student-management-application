<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="login.css"/>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>

    <g:if test="${flash.message}">
        <div class="error-message">${flash.message}</div>
    </g:if>

    <g:form controller="auth" action="loginFrontend" method="POST">
        <div class="mb-3">
            <label for="username">Username:</label>
            <g:textField name="username" required="true" class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="password">Password:</label>
            <g:passwordField name="password" required="true" class="form-control"/>
        </div>
        <g:submitButton name="Login" value="Login" class="btn"/>
    </g:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>