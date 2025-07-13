<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Create StudentManagementSystem.Subject</title>
</head>
<body>
<h2>Create StudentManagementSystem.Subject</h2>

<g:form url="/subjects/save" method="POST">
    <label>Name:</label>
    <input type="text" name="name" required/><br/>

    <label>Code:</label>
    <input type="text" name="code" required/><br/>

    <label>StudentManagementSystem.Teacher:</label>
    <select name="teacherId" required>
        <g:each in="${teachers}" var="t">
            <option value="${t.id}">${t.name}</option>
        </g:each>
    </select><br/>

    <input type="submit" value="Create StudentManagementSystem.Subject"/>
</g:form>
</body>
</html>
