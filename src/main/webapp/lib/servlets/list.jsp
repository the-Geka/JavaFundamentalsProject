<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MyServlet" %>
<jsp:useBean id="myServlets" type="java.util.Collection<model.MyServlet>" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsList</title>
</head>
<body>
    <h1>List of Servlets</h1>

    <h4><a href="${pageContext.request.contextPath}/lib/servlets/add">Add new servlet</a></h4>
    <h4><a href="${pageContext.request.contextPath}/lib/servlets/?reload">Reload from DB</a></h4>

    <table>
        <tr>
            <th>Tasks</th>
            <th>ID</th>
            <th>Name</th>
        </tr>

        <% for (MyServlet myServlet: myServlets) {%>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/lib/servlets/edit?id=<%=myServlet.getId()%>">Edit</a>
                <a href="${pageContext.request.contextPath}/lib/servlets/delete?id=<%=myServlet.getId()%>">Delete</a>
            </td>
            <td><%=myServlet.getId()%></td>
            <td><%=myServlet.getName()%></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
