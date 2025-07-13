<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Create Student</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="createStudent.css"/>
</head>
<body>
<div class="create-student-container">
    <h2>Create Student</h2>

    <g:form url="/students/save" method="POST">
        <div class="mb-3">
            <label for="userId">User ID:</label>
            <input type="number" name="userId" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="name">Name:</label>
            <input type="text" name="name" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="dob">DOB (yyyy-MM-dd):</label>
            <input type="text" name="dob" required class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="address">Address:</label>
            <input type="text" name="address" required class="form-control"/>
        </div>
        <input type="submit" value="Create Student" class="btn"/>
    </g:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>