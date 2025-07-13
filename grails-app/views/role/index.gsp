<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Manage Roles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="roleList.css"/>
</head>
<body>
<div class="role-list-container">
    <h2>Manage Roles</h2>

    <g:if test="${flash.message}">
        <div class="flash-message ${flash.message?.toLowerCase()?.contains('error') ? 'error' : ''}">${flash.message}</div>
    </g:if>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Authority</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <g:each in="${roles}" var="role">
                <tr>
                    <td>${role.id}</td>
                    <td>${role.authority}</td>
                    <td>
                        <g:link controller="role" action="deleteRole" params="[id: role.id]" class="delete-link" onclick="return confirm('Delete this role?')">Delete</g:link>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>

    <g:link controller="role" action="create" class="create-link">Create New Role</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>