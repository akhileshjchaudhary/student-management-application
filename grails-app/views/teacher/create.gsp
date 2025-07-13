<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Create Teacher</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="createTeacher.css"/>
</head>
<body>
<div class="create-teacher-container">
    <h2>Create Teacher</h2>

    <g:form controller="teacher" action="save" method="POST">
        <div class="mb-3">
            <label for="userId">User ID:</label>
            <g:textField name="userId" required="true" class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="name">Name:</label>
            <g:textField name="name" required="true" class="form-control"/>
        </div>
        <div class="mb-3">
            <label for="phoneNumber">Phone Number:</label>
            <g:textField name="phoneNumber" required="true" class="form-control"/>
        </div>
        <g:submitButton name="Create" value="Create" class="btn"/>
    </g:form>

    <g:link controller="teacher" action="index" class="back-link">Back to List</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>