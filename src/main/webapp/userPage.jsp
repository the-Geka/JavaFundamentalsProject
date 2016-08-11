<%--suppress CheckImageSize --%>
<%@ page import="model.MyUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="myUser" type="model.MyUser" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<jsp:useBean id="myUsersFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="myUsersQueryFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="myUsersMyQueryFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="itsMe" type="java.lang.Boolean" scope="request"/>


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
                <col width="250px">
                <col width="400px">
            </colgroup>
            <tr>
                <td valign="top">
                    <img width="100%" src="${pageContext.request.contextPath}/pic/user-512.png" alt="Изображение универсального пользователя">

                    <br/>

                    <% if(!itsMe) { %>
                        <%if (myUsersFriends.contains(myUser)) {%>
                        <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel friend")%></a>
                        <%} else if (myUsersMyQueryFriends.contains(myUser)) {%>
                        <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel query")%></a>
                        <%} else if (myUsersQueryFriends.contains(myUser)) {%>
                        <a href="${pageContext.request.contextPath}/myFriends?add&id=<%=myUser.getId()%>"><%=l.get("Accept friend query")%></a>
                        <%} else {%>
                        <a href="${pageContext.request.contextPath}/myFriends?add&id=<%=myUser.getId()%>"><%=l.get("Query friend accept")%></a>
                        <%}%>
                        <br/>

                        <a href="${pageContext.request.contextPath}/myMessage&id=<%=myUser.getId()%>"><%=l.get("SendMessage")%></a>
                    <%}%>

                    <br/>

                    <%if (myUsersFriends.size() > 0 ) {%>
                    <table width="100%" border="0">
                        <tr>
                            <th>
                            <div style="text-align: center"><%=l.get("myUsersFriends")%></div>

                            </th>
                        </tr>
                        <% for (MyUser myUserList: myUsersFriends) {%>
                        <tr>
                            <th align="left">
                                <a href="${pageContext.request.contextPath}/?id=<%=myUserList.getId()%>"><%=myUserList.getLastName()%> <%=myUserList.getFirstName()%></a>
                            </th>
                        </tr>
                        <%}%>
                    </table>
                    <%}%>
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
