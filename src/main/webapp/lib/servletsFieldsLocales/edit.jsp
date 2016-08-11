<%@ page import="model.MyServletField" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myServlet" type="model.MyServlet" scope="request"/>
<jsp:useBean id="myLocale" type="model.MyLocale" scope="request"/>
<jsp:useBean id="myServletFieldLocaleDao" type="dao.interfaces.MyServletFieldLocaleDao" scope="request"/>
<jsp:useBean id="myServletFields" type="java.util.Collection<model.MyServletField>" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsListFieldsLocales edit</title>
</head>
<body>
<h1>List Fields for Servlets [<%=myServlet.getName()%>] and Locales [<%=myLocale.getName()%>]</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servletsFieldsLocales/">Back to List of Servlets and Locales</a></h4>
<h4><a href="${pageContext.request.contextPath}/lib/servletsFieldsLocales/edit?reload&rbServletId=<%=myServlet.getId()%>&rbLocaleId=<%=myLocale.getId()%>">Reload from DB</a></h4>
<form method="POST" action="${pageContext.request.contextPath}/lib/servletsFieldsLocales/edit?ok">
    <input type="hidden" name="localeId" title="localeId" value="<%=myLocale.getId()%>">
    <table>
        <tr>
            <th>ServletFieldID</th>
            <th>LocaleID</th>
            <th>Text</th>
        </tr>

        <% for (MyServletField myServletField: myServletFields) {%>
        <tr>
            <td><%=myServletField.getId()%></td>
            <td><%=myServletField.getName()%></td>

            <td><label for="inputText">Text(1000)</label></td>
            <td><input type="text" name="<%=myServletField.getId()%>" id="inputText" maxlength="1000" size="100"
                       value="<%=myServletFieldLocaleDao.getByServletFieldIdAndLocaleId(myServletField.getId(),myLocale.getId())%>"></td>
        </tr>
        <%
            }
        %>
    </table>

    <input type="submit" value="Edit ">
</form>


</body>
</html>
