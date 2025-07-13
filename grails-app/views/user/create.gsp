<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Create User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="createUser.css"/>
</head>
<body>
<div class="create-user-container">
    <h2>Create User</h2>

    <g:if test="${flash.message}">
        <div class="flash-message ${flash.message?.toLowerCase()?.contains('error') ? 'error' : ''}">${flash.message}</div>
    </g:if>

    <g:form controller="user" action="saveUser" method="POST">
        <div class="mb-3">
            <label for="username">Username:</label>
            <g:textField name="username" required="true" class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="password">Password:</label>
            <g:passwordField name="password" required="true" class="form-control"/>
        </div>
        <div>
            <g:submitButton name="create" value="Create User" class="btn"/>
        </div>
    </g:form>

    <g:link controller="user" action="index" class="back-link">Back to List</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>