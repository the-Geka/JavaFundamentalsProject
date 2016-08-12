<%@ page import="model.MyMessage" %>
<%@ page import="model.MyUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<jsp:useBean id="myUsersQueryFriendsSize" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="myChatsUsers" type="java.util.Collection<model.MyMessage>" scope="request"/>
<jsp:useBean id="securityService" type="services.SecurityService" scope="request"/>

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
            <div><a href="${pageContext.request.contextPath}/myFriends"><%=l.get("Menu. My friends")%>(${myUsersQueryFriendsSize})</a></div>
            <div><a href="${pageContext.request.contextPath}/myMessages"><%=l.get("Menu. My message")%></a></div>
            <br/>
            <div><a href="${pageContext.request.contextPath}/?signOut"><%=l.get("Menu. SignOut")%></a></div>
        </td>

        <td valign="top">
            <h3><div style="text-align: center;"><%=l.get("Messages List")%></div></h3>

            <%if (myChatsUsers.size() > 0 ) {%>
            <table width="100%" border="1">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
                <% for (MyMessage myMessage: myChatsUsers) {%>
                <%MyUser temp = securityService.checkAndGetMyUser(myMessage.getUserIdTo()).get();%>
                <tr>
                    <td bgcolor="#ffe4c4">
                        <p>Беседа с <a href="${pageContext.request.contextPath}/myMessages?id=<%=myMessage.getUserIdTo()%>"><%=temp.getLastName()%> <%=temp.getFirstName()%></a> :</p>
                        <%=myMessage.isFrom() ? l.get("I'm") : temp.getLastName() + " " + temp.getFirstName()%>: <%=myMessage.getText()%>
                    </td>
                    <td>
                        <%=myMessage.getDatetime()%>
                    </td>
                </tr>
                <%}%>
            </table>
            <%} else {%>
                <h4><div style="text-align: left;"><%=l.get("Messages List. IsEmpty")%></div></h4>
            <%}%>



        </td>
    </tr>

</table>

<br/>
<div align="center"><a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%></a></div>
</body>
</html>
