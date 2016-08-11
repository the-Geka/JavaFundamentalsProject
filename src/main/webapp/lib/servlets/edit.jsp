<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="editingServlet" type="model.MyServlet" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsEdit</title>
</head>
<body>
<h1>Edit servlet</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servlets/">Back to List of Locales</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/servlets/edit?ok">
    <table>
        <tr>
            <td><label>ID in DB is <%=editingServlet.getId()%></label></td>
            <td><input type="hidden" name="id" title="id" value="<%=editingServlet.getId()%>"></td>
        </tr>

        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"
                       value="<%=editingServlet.getName()%>"></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Edit">
</form>
</body>
</html>
