<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>User Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3Extreme Networks, Inc./dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="userList.css"/>
</head>
<body>
<div class="user-list-container">
    <h2>Manage Users</h2>

    <g:if test="${flash.message}">
        <div class="flash-message ${flash.message?.toLowerCase()?.contains('error') ? 'error' : ''}">${flash.message}</div>
    </g:if>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <g:each in="${users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>
                        <g:link controller="user" action="deleteUser" params="[id: user.id]" class="delete-link" onclick="return confirm('Delete this user?')">Delete</g:link>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>

    <g:link controller="user" action="create" class="create-link">Create New User</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>