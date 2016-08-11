<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MyServlet" %>
<jsp:useBean id="myServlets" type="java.util.Collection<model.MyServlet>" scope="request"/>
<jsp:useBean id="myServletFieldDao" type="dao.interfaces.MyServletFieldDao" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsListFields</title>
</head>
<body>
    <h1>List of Servlets with Fields count</h1>

    <h4><a href="${pageContext.request.contextPath}/lib/servletsFields/?reload">Reload from DB</a></h4>

    <table>
        <tr>
            <th>Tasks</th>
            <th>Name</th>
            <th>Fields count</th>
        </tr>

        <% for (MyServlet myServlet: myServlets) {%>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/lib/servletsFields/editServlet?servletId=<%=myServlet.getId()%>">Edit</a>
            </td>
            <td><%=myServlet.getName()%></td>
            <td><%=myServletFieldDao.getFieldsCountByServletId(myServlet.getId())%></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
