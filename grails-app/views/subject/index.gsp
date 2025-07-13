<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Subjects</title>
</head>
<body>
<h2>Subjects List</h2>

<g:if test="${flash.message}">
    <div style="color: green">${flash.message}</div>
</g:if>

<table border="1" cellpadding="5">
    <tr>
        <th>Name</th>
        <th>Code</th>
        <th>StudentManagementSystem.Teacher</th>
        <th>Actions</th>
    </tr>
    <g:each in="${subjects}" var="s">
        <tr>
            <td>${s.name}</td>
            <td>${s.code}</td>
            <td>${s.teacher?.name}</td>
            <td>
                <g:link controller="subject" action="edit" id="${s.id}">Edit</g:link> |
                <g:link controller="subject" action="delete" id="${s.id}"
                        onclick="return confirm('Delete this subject?')">Delete</g:link>
            </td>
        </tr>
    </g:each>
</table>

<br/>
<g:link controller="subject" action="create">Create New StudentManagementSystem.Subject</g:link>
</body>
</html>
