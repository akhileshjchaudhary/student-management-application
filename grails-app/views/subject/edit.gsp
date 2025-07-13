<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Edit StudentManagementSystem.Subject</title>
</head>
<body>
<h2>Edit StudentManagementSystem.Subject</h2>

<g:form url="/subjects/update" method="POST">
    <input type="hidden" name="id" value="${subject?.id}"/>

    <label>Name:</label>
    <input type="text" name="name" value="${subject?.name}" required/><br/>

    <label>Code:</label>
    <input type="text" name="code" value="${subject?.code}" required/><br/>

    <label>StudentManagementSystem.Teacher:</label>
    <select name="teacherId" required>
        <g:each in="${teachers}" var="t">
            <option value="${t.id}" ${t.id == subject.teacher?.id ? 'selected' : ''}>${t.name}</option>
        </g:each>
    </select><br/>

    <input type="submit" value="Update StudentManagementSystem.Subject"/>
</g:form>
</body>
</html>
