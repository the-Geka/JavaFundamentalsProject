<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>LocalesAdd</title>
</head>
<body>
<h1>Add new locale</h1>

<h4><a href="${pageContext.request.contextPath}/lib/locales/">Back to List of Locales</a></h4>

<form method="POST" action="${pageContext.request.contextPath}/lib/locales/add?ok">
    <table>
        <tr>
            <td><label for="inputName">Name(50)</label></td>
            <td><input type="text" name="name" id="inputName" maxlength="50" size="10"></td>
        </tr>

        <tr>
            <td><label for="inputLanguage">Language(8)</label></td>
            <td><input type="text" name="language" id="inputLanguage" maxlength="8" size="8"></td>
        </tr>

        <tr>
            <td><label for="inputCountry">Country(3)</label></td>
            <td><input type="text" name="country" id="inputCountry" size="3" maxlength="3"></td>
        </tr>

        <tr>
            <td><label for="inputScript">Script(4)</label></td>
            <td><input type="text" name="script" id="inputScript" size="4" maxlength="4"></td>
        </tr>

        <tr>
            <td><label for="inputVariant">Variant(8)</label></td>
            <td><input type="text" name="variant" id="inputVariant" size="8" maxlength="8"></td>
        </tr>

    </table>

    <h1></h1>

    <input type="submit" value="Add">
</form>
</body>
</html>
