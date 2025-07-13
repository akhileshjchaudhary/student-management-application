<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Teacher Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="teacherDashboard.css"/>
</head>
<body>
<div class="teacher-dashboard-container">
    <h2>Welcome, ${teacher?.name ?: 'Teacher'}</h2>

    <g:if test="${flash.message}">
        <div class="flash-message ${flash.message.contains('Error') ? 'error' : 'success'}">${flash.message}</div>
    </g:if>

    <h3>Your Students</h3>
    <g:if test="${students}">
        <table>
            <tr>
                <th>Name</th>
                <th>Actions</th>
            </tr>
            <g:each in="${students}" var="student">
                <tr>
                    <td>${student.name}</td>
                    <td>
                        <g:link controller="attendance" action="create" params="[teacherId: teacher.id]">Take Attendance</g:link>
                    </td>
                </tr>
            </g:each>
        </table>
    </g:if>
    <g:else>
        <p>No students assigned.</p>
    </g:else>

    <h3>Recent Attendance Records</h3>
    <g:if test="${attendanceRecords}">
        <table>
            <tr>
                <th>Date</th>
                <th>Student</th>
                <th>Status</th>
            </tr>
            <g:each in="${attendanceRecords}" var="record">
                <tr>
                    <td>${record.date}</td>
                    <td>${record.student.name}</td>
                    <td>${record.status}</td>
                </tr>
            </g:each>
        </table>
    </g:if>
    <g:else>
        <p>No attendance records found.</p>
    </g:else>

    <g:link controller="auth" action="logoutFrontend" class="logout-link">Logout</g:link>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>