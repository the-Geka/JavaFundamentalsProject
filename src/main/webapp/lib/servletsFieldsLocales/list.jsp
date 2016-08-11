<%@ page import="model.MyServlet" %>
<%@ page import="model.MyLocale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myServlets" type="java.util.Collection<model.MyServlet>" scope="request"/>
<jsp:useBean id="myLocales" type="java.util.Collection<model.MyLocale>" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>ServletsListFieldsLocales</title>
</head>
<body>
<h1>List of Servlets and Locales</h1>

<h4><a href="${pageContext.request.contextPath}/lib/servletsFieldsLocales/?reload">Reload from DB</a></h4>
<form method="GET" action="${pageContext.request.contextPath}/lib/servletsFieldsLocales/edit">

    <label for="rbServletId">Servlets</label>
    <table>
        <% for (MyServlet myServlet: myServlets) {%>
        <tr>
            <td><input type="radio" id="rbServletId" name="rbServletId" value="<%=myServlet.getId()%>"><%=myServlet.getName()%></td>
        </tr>
        <%
            }
        %>
    </table>
    <br>
    <label for="rbLocaleId">Locales</label>
    <table>
        <% for (MyLocale myLocale: myLocales) {%>
        <tr>
            <td><input type="radio" id="rbLocaleId" name="rbLocaleId" value="<%=myLocale.getId()%>"><%=myLocale.getName()%> [<%=myLocale.getLanguage()%> <%=myLocale.getCountry()%> <%=myLocale.getVariant()%> <%=myLocale.getScript()%>]</td>
        </tr>
        <%
            }
        %>
    </table>
    <br>
    <input type="submit" value="Edit ">
</form>


</body>
</html>
