<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="myUser" type="model.MyUser" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>

<html>
<head>
    <meta charset="UTF-8">
    <title><%=l.get("Title")%></title>
</head>
<body>
<h1><div style="text-align: center;"><%=l.get("Title")%></div></h1>
<table width="800" align="center" border="0">
    <colgroup>
        <col width="200">
        <col width="650">
    </colgroup>
    <tr>
        <td valign="top">
            <div><a href="${pageContext.request.contextPath}/"><%=l.get("Menu. My page")%></a></div>
            <div><a href="${pageContext.request.contextPath}/myFriends"><%=l.get("Menu. My friends")%></a></div>
            <div><a href="${pageContext.request.contextPath}/myMessages"><%=l.get("Menu. My message")%></a></div>
            <br/>
            <div><a href="${pageContext.request.contextPath}/?signOut"><%=l.get("Menu. SignOut")%></a></div>
        </td>

        <td valign="top">${myUser.firstName} ${myUser.lastName}
        <table width="100%" border="0">
            <colgroup>
                <col width="250">
                <col width="400">
            </colgroup>
            <tr>
                <td valign="top">
                    TODO MY FRIENDS Short list
                </td>
                <td valign="top">
                    <%=l.get("User email:")%> ${myUser.email}
                </td>
            </tr>
        </table>
        </td>
    </tr>

</table>

<br/>
<div align="right"><a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%></a></div>

</body>
</html>
