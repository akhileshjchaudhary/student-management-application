<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="adminDashboard.css"/>
</head>
<body>
<div class="admin-dashboard-container">
    <h1>Welcome, Admin!</h1>
    <ul>
        <li><a href="${createLink(controller: 'teacher', action: 'index')}" class="btn">Manage Teachers</a></li>
        <li><a href="${createLink(controller: 'student', action: 'index')}" class="btn">Manage Students</a></li>
        <li><a href="${createLink(controller: 'user', action: 'index')}" class="btn">Manage Users</a></li>
        <li><a href="${createLink(controller: 'role', action: 'index')}" class="btn">Manage Roles</a></li>
        <li><a href="${createLink(controller: 'teacherStudent', action: 'assignForm')}" class="btn">Assign Students</a></li>
    </ul>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>