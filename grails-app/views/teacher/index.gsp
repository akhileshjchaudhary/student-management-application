<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Teachers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="teacherList.css"/>
</head>
<body>
<div class="teacher-list-container">
    <h2>Teacher List</h2>

    <g:if test="${flash.message}">
        <div class="flash-message">${flash.message}</div>
    </g:if>

    <table>
        <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>Phone Number</th>
            <th>Delete</th>
            <th>Update</th>
        </tr>
        <g:each in="${teachers}" var="t">
            <tr>
                <td>${t.userId}</td>
                <td>${t.name}</td>
                <td>${t.phoneNumber}</td>
                <td>
                    <g:link controller="teacher" action="delete" id="${t.id}" class="delete-link" onclick="return confirm('Delete this teacher?')">Delete</g:link>
                </td>
                <td>
                    <g:link controller="teacher" action="editTeacher" id="${t.id}">Update</g:link>
                </td>
            </tr>
        </g:each>
    </table>

    <g:link controller="teacher" action="create" class="create-link">Create New Teacher</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>