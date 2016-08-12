<%@ page import="model.MyMessage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<jsp:useBean id="myUsersQueryFriendsSize" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="myChatUser" type="model.MyUser" scope="request"/>
<jsp:useBean id="pagger" type="java.util.Collection<java.lang.Integer> " scope="request"/>
<jsp:useBean id="myMessages" type="java.util.Collection<model.MyMessage>" scope="request"/>


<html>
<head>
    <meta charset="UTF-8">
    <title><%=l.get("Title")%>
    </title>
</head>
<body>
<h1>
    <div style="text-align: center;"><%=l.get("Title")%>
    </div>
</h1>
<table width="800" align="center" border="0">
    <colgroup>
        <col width="200">
        <col width="650">
    </colgroup>
    <tr>
        <td valign="top">
            <div><a href="${pageContext.request.contextPath}/"><%=l.get("Menu. My page")%></a></div>
            <div><a href="${pageContext.request.contextPath}/myFriends"><%=l.get("Menu. My friends")%>(${myUsersQueryFriendsSize})</a></div>
            <div><a href="${pageContext.request.contextPath}/myMessages"><%=l.get("Menu. My message")%>
            </a></div>
            <br/>
            <div><a href="${pageContext.request.contextPath}/?signOut"><%=l.get("Menu. SignOut")%>
            </a></div>
        </td>

        <td valign="top">
            <h3>
                <div style="text-align: center;"><%=l.get("Messages with user")%> ${myChatUser.firstName} ${myChatUser.lastName}</div>
            </h3>
            <%if (myMessages.size() == 0) {%>
            <h4>
                <div style="text-align: left;"><%=l.get("Messages with user. IsEmpty")%>
                </div>
            </h4>
            <%} else {%>
            <table width="100%" border="0">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
                <% for (MyMessage myMessage : myMessages) {%>
                <tr>
                    <td rowspan="2" bgcolor="<%=myMessage.isFrom() ?"#ffe4c4":"#e0ffff"%>">
                        <%=myMessage.isFrom() ? l.get("I'm") : myChatUser.getLastName() + " " + myChatUser.getFirstName()%>: <%=myMessage.getText()%>
                    </td>
                    <td>
                        <%=myMessage.getDatetime()%>
                    </td>
                </tr>
                <tr>
                    <td>
                        <%=myMessage.isRead() ? "" : "NEW" %>
                    </td>
                </tr>

                <%}%>
            </table>
            <%}%>

            <form method="POST" action="${pageContext.request.contextPath}?id=${myChatUser.id}">
                <p><label for="msg"><%=l.get("TextMessage")%> (128)</label></p>
                <p><textarea rows="6" cols="60" name="msg" id="msg" maxlength="128"></textarea></p>
                <p><input type="submit" value="<%=l.get("Submit")%>"/></p>
            </form>

            <h4 style="text-align: left;"><%=l.get("Pages")%></h4>
            <% for (int aInt: pagger) {%>
                <%if (aInt==0) {%>
                    <%=l.get("PageDelemiter")%>
                <%} else {%>
                    <a href="${pageContext.request.contextPath}?id=${myChatUser.id}&offset=<%=aInt%>"><%=aInt%></a>
                <%}%>
            <%}%>

        </td>
    </tr>

</table>

<br/>
<div align="center"><a
        href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%>
</a></div>
</body>
</html>
