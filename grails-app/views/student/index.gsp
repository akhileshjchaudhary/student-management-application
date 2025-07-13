<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Students</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="studentList.css"/>
</head>
<body>
<div class="student-list-container">
    <h2>Student List</h2>

    <g:if test="${flash.message}">
        <div class="flash-message">${flash.message}</div>
    </g:if>

    <table>
        <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>DOB</th>
            <th>Address</th>
            <th>Actions</th>
        </tr>
        <g:each in="${students}" var="s">
            <tr>
                <td>${s.user?.id}</td>
                <td>${s.name}</td>
                <td>${s.dob}</td>
                <td>${s.address}</td>
                <td>
                    <g:link controller="student" action="edit" id="${s.id}">Edit</g:link> |
                    <g:link controller="student" action="delete" id="${s.id}" class="delete-link" onclick="return confirm('Delete this student?')">Delete</g:link>
                </td>
            </tr>
        </g:each>
    </table>

    <g:link controller="student" action="create" class="create-link">Create New Student</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>