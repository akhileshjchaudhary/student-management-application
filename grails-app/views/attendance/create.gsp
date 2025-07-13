<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" />
<title>Take StudentManagementSystem.Attendance</title></head>
<body>

<h2>Take StudentManagementSystem.Attendance</h2>

<g:if test="${flash.message}">
    <div style="color: green">${flash.message}</div>
</g:if>

<g:form url="/attendances/create" method="GET">
    <label>Select StudentManagementSystem.Teacher:</label>
    <select name="teacherId" onchange="this.form.submit()">
        <option value="">-- Select --</option>
        <g:each in="${allTeachers}" var="t">
            <option value="${t.id}" ${t.id == teacher?.id ? 'selected' : ''}>${t.name}</option>
        </g:each>
    </select>
</g:form>

<g:if test="${teacher && students}">
    <br/>
    <g:form url="/attendances/save" method="POST">
        <input type="hidden" name="teacherId" value="${teacher.id}" />

        <label>Date:</label>
        <input type="date" name="date" required /><br/><br/>

        <table border="1" cellpadding="5">
            <tr>
                <th>StudentManagementSystem.Student Name</th>
                <th>Status</th>
            </tr>
            <g:each in="${students}" var="s">
                <tr>
                    <td>${s.name}</td>
                    <td>
                        <select name="status_${s.id}">
                            <option value="Present">Present</option>
                            <option value="Absent">Absent</option>
                            <option value="Leave">Leave</option>
                        </select>
                    </td>
                </tr>
            </g:each>
        </table>

        <br/>
        <input type="submit" value="Submit StudentManagementSystem.Attendance" />
    </g:form>
</g:if>

</body>
</html>
