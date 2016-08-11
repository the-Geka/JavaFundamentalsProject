<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsAdd</title>
</head>
<body>
<h1>Add new servlet</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servlets/">Back to List of Servlets</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/servlets/add?ok">
    <table>
        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Add">
</form>
</body>
</html>
