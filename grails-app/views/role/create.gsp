<!DOCTYPE html>
<html>
<head>
    <title>Create Role</title>
</head>
<body>
<h2>Create New Role</h2>

<g:form controller="role" action="saveRole" method="POST">
    <div>
        <label>Authority:</label>
        <g:textField name="authority"/>
    </div>
    <div>
        <g:submitButton name="create" value="Create Role"/>
    </div>
</g:form>

<g:link controller="role" action="index">Back to List</g:link>
</body>
</html>
