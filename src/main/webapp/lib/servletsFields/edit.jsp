<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myServlet" type="model.MyServlet" scope="request"/>
<jsp:useBean id="editingServletField" type="model.MyServletField" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsFieldEdit</title>
</head>
<body>
<h1>Edit servlet field</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servletsFields/editServlet?&servletId=<%=myServlet.getId()%>">Back to List of Servlets</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/servletsFields/edit?ok">
    <table>
        <tr>
            <td><label>ID in DB is <%=editingServletField.getId()%></label></td>
            <td><input type="hidden" name="id" title="id" value="<%=editingServletField.getId()%>"></td>
        </tr>

        <tr>
            <td><label>ServletID in DB is <%=myServlet.getId()%></label></td>
            <td><input type="hidden" name="servletId" title="servletId" value="<%=myServlet.getId()%>"></td>
        </tr>

        <tr>
            <td><label>Servlet Name is <%=myServlet.getName()%></label></td>
        </tr>

        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"
                       value="<%=editingServletField.getName()%>"></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Edit">
</form>
</body>
</html>
