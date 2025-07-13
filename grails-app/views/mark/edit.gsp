<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
    <title>Edit StudentManagementSystem.Mark</title>
</head>
<body>
<h2>Edit StudentManagementSystem.Mark</h2>

<g:form url="/marks/update" method="POST">
    <input type="hidden" name="id" value="${mark?.id}" />

    <label>Marks Obtained:</label>
    <input type="number" name="marksObtained" value="${mark?.marksObtained}" min="0" max="100" required /><br/>

    <label>Date:</label>
    <input type="date" name="date" value="${mark?.date?.format('yyyy-MM-dd')}" required /><br/>

    <input type="submit" value="Update StudentManagementSystem.Mark" />
</g:form>

</body>
</html>
