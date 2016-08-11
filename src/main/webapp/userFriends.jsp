<%--suppress HtmlUnknownTag --%>
<%@ page import="model.MyUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="l" type="services.MyLocaleServiceHelper" scope="request"/>
<jsp:useBean id="requestedUrl" type="java.lang.String" scope="request"/>
<jsp:useBean id="myUsersQueryFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="myUsersFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="myUsersMyQueryFriends" type="java.util.Collection<model.MyUser>" scope="request"/>
<jsp:useBean id="myUsersFindResult" type="java.util.Collection<model.MyUser>" scope="request"/>
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

        <td valign="top">
            <form method="POST" action="${pageContext.request.contextPath}/myFriends">

                        <label for="inputFindStr"><%=l.get("Find friends")%></label>
                        <input type="text" name="inputFindStr" id="inputFindStr" maxlength="30" size="30">

                <input type="submit" value="<%=l.get("Find")%>">
            </form>


            <%if (myUsersFindResult.size() > 0 ) {%>
            <h3><div style="text-align: center;"><%=l.get("myUsersFindResult")%></div></h3>
            <table width="100%" border="1">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
            <% for (MyUser myUser: myUsersFindResult) {%>
                <tr>
                    <th rowspan="2" bgcolor="#ffe4c4">
                        <a href="${pageContext.request.contextPath}/?id=<%=myUser.getId()%>"><%=myUser.getLastName()%> <%=myUser.getFirstName()%></a>
                    </th>
                    <td>
                        <%if (myUsersFriends.contains(myUser)) {%>
                            <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel friend")%></a>
                        <%} else if (myUsersMyQueryFriends.contains(myUser)) {%>
                            <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel query")%></a>
                        <%} else if (myUsersQueryFriends.contains(myUser)) {%>
                            <a href="${pageContext.request.contextPath}/myFriends?add&id=<%=myUser.getId()%>"><%=l.get("Accept friend query")%></a>
                        <%} else {%>
                            <a href="${pageContext.request.contextPath}/myFriends?add&id=<%=myUser.getId()%>"><%=l.get("Query friend accept")%></a>
                        <%}%>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/myMessage&id=<%=myUser.getId()%>"><%=l.get("SendMessage")%></a>
                    </td>
                </tr>
            <%}%>
            </table>
            <%}%>


            <%if (myUsersQueryFriends.size() > 0 ) {%>
            <h3><div style="text-align: center;"><%=l.get("myUsersQueryFriends")%></div></h3>
            <table width="100%" border="1">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
                <% for (MyUser myUser: myUsersQueryFriends) {%>

                <tr>
                    <th rowspan="2" bgcolor="#ffe4c4">
                        <a href="${pageContext.request.contextPath}/?id=<%=myUser.getId()%>"><%=myUser.getLastName()%> <%=myUser.getFirstName()%></a>
                    </th>
                    <td>
                        <a href="${pageContext.request.contextPath}/myFriends?add&id=<%=myUser.getId()%>"><%=l.get("Accept friend query")%></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/myMessage&id=<%=myUser.getId()%>"><%=l.get("SendMessage")%></a>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>



            <%if (myUsersFriends.size() > 0 ) {%>
            <h3><div style="text-align: center;"><%=l.get("myUsersFriends")%></div></h3>
            <table width="100%" border="1">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
                <% for (MyUser myUser: myUsersFriends) {%>

                <tr>
                    <th rowspan="2" bgcolor="#8fbc8f">
                        <a href="${pageContext.request.contextPath}/?id=<%=myUser.getId()%>"><%=myUser.getLastName()%> <%=myUser.getFirstName()%></a>
                    </th>
                    <td>
                        <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel friend")%></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/myMessage&id=<%=myUser.getId()%>"><%=l.get("SendMessage")%></a>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>




            <%if (myUsersMyQueryFriends.size() > 0 ) {%>
            <h3><div style="text-align: center;"><%=l.get("myUsersMyQueryFriends")%></div></h3>
            <table width="100%" border="1">
                <colgroup>
                    <col width="440">
                    <col width="200">
                </colgroup>
                <% for (MyUser myUser: myUsersMyQueryFriends) {%>

                <tr>
                    <th rowspan="2" bgcolor="yellow">
                        <a href="${pageContext.request.contextPath}/?id=<%=myUser.getId()%>"><%=myUser.getLastName()%> <%=myUser.getFirstName()%></a>
                    </th>
                    <td>
                        <a href="${pageContext.request.contextPath}/myFriends?cancel&id=<%=myUser.getId()%>"><%=l.get("Cancel query")%></a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/myMessage&id=<%=myUser.getId()%>"><%=l.get("SendMessage")%></a>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>




        </td>
    </tr>

</table>

<br/>
<div align="right"><a href="${pageContext.request.contextPath}/localeSelector?requestedUrl=${requestedUrl}"><%=l.get("LANGUAGE")%></a></div>

</body>
</html>