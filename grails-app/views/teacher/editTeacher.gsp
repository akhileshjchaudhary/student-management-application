<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Edit Teacher</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="editTeacher.css"/>
</head>
<body>
<div class="edit-teacher-container">
    <h2>Edit Teacher</h2>

    <g:form url="/teachers/update" method="POST">
        <input type="hidden" name="id" value="${teacher?.id}"/>
        <div class="mb-3">
            <label for="name">Name:</label>
            <input type="text" name="name" value="${teacher?.name}" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="phoneNumber">Phone Number:</label>
            <input type="text" name="phoneNumber" value="${teacher?.phoneNumber}" required class="form-control"/>
        </div>
        <input type="submit" value="Update Teacher" class="btn"/>
    </g:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>