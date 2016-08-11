<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.MyLocale" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="myLocales" type="java.util.Collection<model.MyLocale>" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>LocalesList</title>
</head>
<body>
    <h1><%=l.get("List of Locales")%></h1>

    <h4><a href="${pageContext.request.contextPath}/lib/locales/add"><%=l.get("Add new locale")%></a></h4>
    <h4><a href="${pageContext.request.contextPath}/lib/locales/?reload"><%=l.get("Reload from DB")%></a></h4>

    <table>
        <tr>
            <th><%=l.get("Tasks")%></th>
            <th><%=l.get("ID")%></th>
            <th><%=l.get("Name")%></th>
            <th><%=l.get("Language")%></th>
            <th><%=l.get("Country")%></th>
            <th><%=l.get("Script")%></th>
            <th><%=l.get("Variant")%></th>
        </tr>

        <% for (MyLocale myLocale: myLocales) {%>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/lib/locales/edit?id=<%=myLocale.getId()%>"><%=l.get("Edit")%></a>
                <a href="${pageContext.request.contextPath}/lib/locales/delete?id=<%=myLocale.getId()%>"><%=l.get("Delete")%></a>
            </td>
            <td><%=myLocale.getId()%></td>
            <td><%=myLocale.getName()%></td>
            <td><%=myLocale.getLanguage()%></td>
            <td><%=myLocale.getCountry()%></td>
            <td><%=myLocale.getScript()%></td>
            <td><%=myLocale.getVariant()%></td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
