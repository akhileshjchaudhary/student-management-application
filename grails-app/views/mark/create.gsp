<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Add StudentManagementSystem.Mark</title>
</head>
<body>

<h2>Add StudentManagementSystem.Mark</h2>

<g:form url="/marks/save" method="POST">
    <label>StudentManagementSystem.Student:</label>
    <select name="studentId" required>
        <option value="" disabled selected>Select StudentManagementSystem.Student</option>
        <g:each in="${students}" var="s">
            <option value="${s.id}">${s.name}</option>
        </g:each>
    </select><br/>

    <label>StudentManagementSystem.Subject:</label>
    <select name="subjectId" required>
        <option value="" disabled selected>Select StudentManagementSystem.Subject</option>
        <g:each in="${subjects}" var="sub">
            <option value="${sub.id}">${sub.name}</option>
        </g:each>
    </select><br/>

    <label>Marks Obtained:</label>
    <input type="number" name="marksObtained" min="0" max="100" required /><br/>

    <label>Date:</label>
    <input type="date" name="date" required /><br/>

    <input type="submit" value="Save StudentManagementSystem.Mark" />
</g:form>

</body>
</html>
