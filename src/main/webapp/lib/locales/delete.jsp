<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="deletingLocale" type="model.MyLocale" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <title>LocalesDelete</title>
</head>
<body>
<h1>Delete locale confirmation</h1>

<h4><a href="${pageContext.request.contextPath}/lib/locales/">Back to List of Locales</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/locales/delete?ok">
    <table>
        <tr>
            <td><label>ID in DB is <%=deletingLocale.getId()%></label></td>
            <td><input type="hidden" name="id" title="id" value="<%=deletingLocale.getId()%>"></td>
        </tr>

        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"
                       value="<%=deletingLocale.getName()%>" disabled></td>
        </tr>

        <tr>
            <td><label for="inputLanguage">Language(8)</label></td>
            <td><input type="text" name="language" id="inputLanguage" maxlength="8" size="8"
                       value="<%=deletingLocale.getLanguage()%>" disabled></td>
        </tr>

        <tr>
            <td><label for="inputCountry">Country(3)</label></td>
            <td><input type="text" name="country" id="inputCountry" size="3" maxlength="3"
                       value="<%=deletingLocale.getCountry()%>" disabled></td>
        </tr>

        <tr>
            <td><label for="inputScript">Script(4)</label></td>
            <td><input type="text" name="script" id="inputScript" size="4" maxlength="4"
                       value="<%=deletingLocale.getScript()%>" disabled></td>
        </tr>

        <tr>
            <td><label for="inputVariant">Variant(8)</label></td>
            <td><input type="text" name="variant" id="inputVariant" size="8" maxlength="8"
                       value="<%=deletingLocale.getVariant()%>" disabled></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Delete">
</form>
</body>
</html>
