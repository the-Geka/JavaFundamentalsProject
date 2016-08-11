<%@ page import="model.MyLocale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myLocales" type="java.util.Collection<model.MyLocale>" scope="request"/>
<jsp:useBean id="currentEditedLocale" type="model.MyLocale" scope="request"/>
<html>
<head>
    <title>LocalesView</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/first.css" type="text/css">
</head>
<body>

    <h1>List Of Locales</h1>
    <h3><a href="${pageContext.request.contextPath}/t/locales?reload">reload</a></h3>

    <%--<form method="POST" action="${requestURI}">--%>
    <form method="POST" action="${pageContext.request.contextPath}/t/locales?update">

        <label for="inputName">id</label>
        <input type="text" name="id" title="id" size="2" value="<%=currentEditedLocale.getId()%>">

        <label for="inputName">Название локали</label>
        <input class="required" type="text" name="name" id="inputName" value="<%=currentEditedLocale.getName()%>">

        <label for="inputLanguage">Язык</label>
        <input class="required" type="text" name="language" id="inputLanguage" size="8" value="<%=currentEditedLocale.getLanguage()%>">

        <label for="inputCountry">Страна</label>
        <input type="text" name="country" id="inputCountry" size="3" value="<%=currentEditedLocale.getCountry()%>">

        <label for="inputScript">Скрипт</label>
        <input type="text" name="script" id="inputScript" size="4" value="<%=currentEditedLocale.getScript()%>">

        <label for="inputVariant">Вариант</label>
        <input type="text" name="variant" id="inputVariant" size="8"  value="<%=currentEditedLocale.getVariant()%>">

        <input type="submit" value="ОК">

    </form>

    <table>
        <tr>
            <th>Task</th>
            <th>id</th>
            <th>Name</th>
            <th>Language</th>
            <th>Country</th>
            <th>Script</th>
            <th>Variant</th>
        </tr>

        <% for (MyLocale myLocale: myLocales) {%>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/t/locales?update&id=-1">Add</a>
                <a href="${pageContext.request.contextPath}/t/locales?update&id=<%=myLocale.getId()%>">Edit</a>
                <a href="${pageContext.request.contextPath}/t/locales?delete&id=<%=myLocale.getId()%>">Delete</a>
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
