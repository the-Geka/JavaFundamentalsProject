package testers;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/t/authentication")
public class Authentication extends HttpServlet {
    private ServletConfig config;

    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out2 = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);

        String connectionURL =  "jdbc:sqlserver://home:1433;" +
                                "databaseName=Test;user=sa;password=qwe123@";
        Connection connection;
        ResultSet rs;
        String userName = "empty";
        String passwrd = "empty";
        response.setContentType("text/plain; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        try {
            //Загружаем драйвер БД
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Получаем подключение к базе данных
            connection = DriverManager.getConnection(connectionURL);
            //Заносим данные в базу
            out2.println(java.time.LocalDateTime.now().toString());
            String sql = "SELECT * FROM [dbo].[UT]";
            Statement s = connection.createStatement();
            s.executeQuery(sql);
            rs = s.getResultSet();
            while (rs.next()) {
                userName = rs.getString("name");
                passwrd = rs.getString("password");
            }
            rs.close();
            s.close();
        } catch (Exception e) {
            System.out.println("Exception is ;" + e);
            out2.println("Exception is ;" + e);

        }
        if (userName.equals(request.getParameter("user")) &&
                passwrd.equals(request.getParameter("pass"))) {
            out2.println("User Authenticated");
        } else {
            out2.println("You are not an authentic person");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}