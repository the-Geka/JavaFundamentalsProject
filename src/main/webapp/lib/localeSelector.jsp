<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MyLocale" %>
<%@ page import="static controllers.localeSelector.LocaleSelector.MYLOCALE_ID" %>
<jsp:useBean id="myLocales" type="java.util.Collection<model.MyLocale>" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>Languages</title>
</head>
<body>
<h4>Languages</h4>
<table>
    <% for (MyLocale myLocale: myLocales) {%>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}&<%=MYLOCALE_ID%>=<%=myLocale.getId()%>"><%=myLocale.getName()%></a>
        </td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
