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

<h1><%=l.get("Login page")%></h1>
<h3>${actionStatus}</h3>

<form method="POST" action="${requestedUrl}">
    <table>
        <tr>
            <td><label for="email"><%=l.get("Email")%></label></td>
            <td><input type="text" name="j_username" id="email" size="40"></td>
        </tr>

        <tr>
            <td><label for="password"><%=l.get("Password")%></label></td>
            <td><input type="password" name="j_password" id="password" size="40" autocomplete="off"></td>
        </tr>
        <%--<%=l.get("Email")%> <input type="text" name="j_username" title="<%=l.get("Email")%>"/><br/>--%>
        <%--<%=l.get("Password")%><input type="password" name="j_password" autocomplete="off" title="<%=l.get("Password")%>"/><br/>--%>
    </table>
    <input type="submit" value="<%=l.get("Submit")%>"/>
</form>

<h4><a href="${pageContext.request.contextPath}/signUp?requestedUrl=${requestedUrl}"><%=l.get("Sign up")%></a></h4>

<h4><a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%></a></h4>


</body>
</html>
