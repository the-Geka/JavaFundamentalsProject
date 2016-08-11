<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MyServletField" %>
<jsp:useBean id="myServletFields" type="java.util.Collection<model.MyServletField>" scope="request"/>
<jsp:useBean id="myServlet" type="model.MyServlet" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsListFields</title>
</head>
<body>
    <h1>List of Servlet Fields <%=myServlet.getName()%></h1>

    <h4><a href="${pageContext.request.contextPath}/lib/servletsFields/">Back to List of ServletsFields</a></h4>
    <h4><a href="${pageContext.request.contextPath}/lib/servletsFields/editServlet?reload&servletId=<%=myServlet.getId()%>">Reload from DB</a></h4>
    <h4><a href="${pageContext.request.contextPath}/lib/servletsFields/add?&servletId=<%=myServlet.getId()%>">Add new field to servlet</a></h4>

    <table>
        <tr>
            <th>Tasks</th>
            <th>ID</th>
            <th>ServletID</th>
            <th>Name</th>
        </tr>

        <% for (MyServletField myServletField: myServletFields) {%>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/lib/servletsFields/edit?id=<%=myServletField.getId()%>&servletId=<%=myServletField.getServletId()%>">Edit</a>
                <a href="${pageContext.request.contextPath}/lib/servletsFields/delete?id=<%=myServletField.getId()%>&servletId=<%=myServletField.getServletId()%>">Delete</a>
            </td>
            <td><%=myServletField.getId()%></td>
            <td><%=myServletField.getServletId()%></td>
            <td><%=myServletField.getName()%></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
