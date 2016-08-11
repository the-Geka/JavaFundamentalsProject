<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="deletingServlet" type="model.MyServlet" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsDelete</title>
</head>
<body>
<h1>Delete servlet confirmation</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servlets/">Back to List of Locales</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/servlets/delete?ok">
    <table>
        <tr>
            <td><label>ID in DB is <%=deletingServlet.getId()%></label></td>
            <td><input type="hidden" name="id" title="id" value="<%=deletingServlet.getId()%>"></td>
        </tr>

        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"
                       value="<%=deletingServlet.getName()%>" disabled></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Delete">
</form>
</body>
</html>
