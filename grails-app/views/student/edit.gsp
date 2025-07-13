<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Edit Student</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="editStudent.css"/>
</head>
<body>
<div class="edit-student-container">
    <h2>Edit Student</h2>

    <g:form url="/students/update" method="POST">
        <input type="hidden" name="id" value="${student?.id}"/>
        <div class="mb-3">
            <label for="name">Name:</label>
            <input type="text" name="name" value="${student?.name}" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="dob">DOB (yyyy-MM-dd):</label>
            <input type="text" name="dob" value="${student?.dob}" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="address">Address:</label>
            <input type="text" name="address" value="${student?.address}" required class="form-control"/>
        </div>
        <input type="submit" value="Update Student" class="btn"/>
    </g:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>