<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Marks</title>
</head>
<body>
<h2>All Marks</h2>

<g:if test="${flash.message}">
    <div style="color: green">${flash.message}</div>
</g:if>

<table border="1" cellpadding="5">
    <tr>
        <th>StudentManagementSystem.Student Name</th>
        <th>StudentManagementSystem.Subject</th>
        <th>Marks Obtained</th>
        <th>Date</th>
        <th>Delete</th>
        <th>Edit</th> <!-- New Column -->
    </tr>
    <g:each in="${marks}" var="m">
        <tr>
            <td>${m.student.name}</td>
            <td>${m.subject.name}</td>
            <td>${m.marksObtained}</td>
            <td>${m.date}</td>
            <td>
                <g:link controller="mark" action="delete" id="${m.id}"
                        onclick="return confirm('Delete this mark?')">Delete</g:link>
            </td>
            <td>
                <g:link controller="mark" action="edit" id="${m.id}">Edit</g:link>
            </td>
        </tr>
    </g:each>

</table>

<br/>
<g:link controller="mark" action="create">Add StudentManagementSystem.Mark</g:link>

</body>
</html>
