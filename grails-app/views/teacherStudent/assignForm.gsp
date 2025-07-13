<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Assign Students to Teacher</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <asset:stylesheet src="assignTeacherStudents.css"/>
</head>
<body>
<div class="assign-teacher-students-container">
    <h2>Assign Students to Teacher</h2>

    <g:if test="${flash.message}">
        <div class="flash-message ${flash.message?.toLowerCase()?.contains('error') ? 'error' : ''}">${flash.message}</div>
    </g:if>

    <g:form url="/teacherStudents/assign" method="POST">
        <div class="mb-3">
            <label for="teacherId">Choose Teacher:</label>
            <select name="teacherId" id="teacherId" required class="form-control">
                <option value="">-- Select --</option>
                <g:each in="${teachers}" var="t">
                    <option value="${t.id}">${t.name}</option>
                </g:each>
            </select>
        </div>
        <div class="mb-3">
            <label for="studentIds">Choose Students:</label>
            <select name="studentIds" id="studentIds" multiple="multiple" required class="form-control">
                <g:each in="${students}" var="s">
                    <option value="${s.id}">${s.name}</option>
                </g:each>
            </select>
        </div>
        <input type="submit" value="Assign Students" class="btn"/>
    </g:form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>