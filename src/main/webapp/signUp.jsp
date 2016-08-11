<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="actionStatus" type="java.lang.String" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<html>
<head>
    <meta charset="UTF-8">
    <title><%=l.get("Title")%></title>
</head>
<body>

<h1><%=l.get("Sign up page")%></h1>
<h3>${actionStatus}</h3>

<form method="POST" action="${pageContext.request.contextPath}/signUp?requestedUrl=${requestedUrl}">
    <table>
        <tr>
            <td><label for="email"><%=l.get("Email")%></label></td>
            <td><input type="text" name="email" id="email" size="40" maxlength="256"></td>
        </tr>

        <tr>
            <td><label for="password"><%=l.get("Password")%></label></td>
            <td><input type="password" name="password" id="password" size="40" maxlength="16" autocomplete="off"></td>
        </tr>

        <tr>
            <td><label for="passwordConf"><%=l.get("Password confirm")%></label></td>
            <td><input type="password" name="passwordConf" id="passwordConf" size="40" maxlength="16" autocomplete="off"></td>
        </tr>

        <tr>
            <td><label for="lastName"><%=l.get("lastName")%></label></td>
            <td><input type="text" name="lastName" id="lastName" size="40" maxlength="30"></td>
        </tr>

        <tr>
            <td><label for="firstName"><%=l.get("firstName")%></label></td>
            <td><input type="text" name="firstName" id="firstName" size="40" maxlength="30"></td>
        </tr>

    </table>
    <input type="submit" value="<%=l.get("Submit")%>"/>
</form>



<h4><a href="${pageContext.request.contextPath}${requestedUrl}"><%=l.get("Sign in")%></a></h4>

<h4><a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=/signUp?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%></a></h4>


</body>
</html>
