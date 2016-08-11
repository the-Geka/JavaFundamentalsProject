<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myServlet" type="model.MyServlet" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletFieldsAdd</title>
</head>
<body>
<h1>Add new field for servlet ID=<%=myServlet.getId()%> Name=<%=myServlet.getName()%></h1>

<h4><a href="${pageContext.request.contextPath}/lib/servletsFields/editServlet?&servletId=<%=myServlet.getId()%>">Back to List of Servlets</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/servletsFields/add?ok">
    <table>
        <tr>
            <td><input type="hidden" name="servletId" title="servletId" value="<%=myServlet.getId()%>"></td>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="30"></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Add">
</form>
</body>
</html>
