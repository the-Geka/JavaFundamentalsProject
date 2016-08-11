package listeners;

import dao.interfaces.*;
import dao.mssql.*;
import services.MyLocaleService;
import services.SecurityService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

@WebListener
public class DaoProvider implements ServletContextListener {

    public static final String MYLOCALE_DAO = "myLocaleDao";
    public static final String MYSERVLET_DAO = "myServletDao";
    public static final String MYSERVLETFIELD_DAO = "myServletFieldDao";
    public static final String MYSERVLETFIELDLOCALE_DAO = "myServletFieldLocaleDao";
    public static final String MYUSER_DAO = "myUserDao";

    public static final String MYLOCALE_SERVICE = "myLocaleService";
    public static final String SECURITY_SERVICE = "securityService";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String connectionUrl =
                "jdbc:sqlserver://home:1433;" +
                        "databaseName=Test2;user=sa;password=qwe123@";

        try {
            Class.forName(driver);
            Supplier<Connection> connectionSupplier = () -> {
                try {
                    return DriverManager.getConnection(connectionUrl);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };

            ServletContext servletContext = sce.getServletContext();

            MyLocaleDao myMyLocaleDao = new MssqlMyLocaleDao(connectionSupplier);
            MyServletDao myServletDao = new MssqlMyServletDao(connectionSupplier);
            MyServletFieldDao myServletFieldDao = new MssqlMyServletFieldDao(connectionSupplier);
            MyServletFieldLocaleDao myServletFieldLocaleDao = new MssqlMyServletFieldLocaleDao(connectionSupplier);
            MyUserDao myUserDao = new MssqlMyUserDao(connectionSupplier);

            MyLocaleService myLocaleService = new MyLocaleService(myMyLocaleDao, myServletDao, myServletFieldDao,
                    myServletFieldLocaleDao);
            SecurityService securityService = new SecurityService(myUserDao);


            servletContext.setAttribute(MYLOCALE_DAO, myMyLocaleDao);
            servletContext.setAttribute(MYSERVLET_DAO, myServletDao);
            servletContext.setAttribute(MYSERVLETFIELD_DAO, myServletFieldDao);
            servletContext.setAttribute(MYSERVLETFIELDLOCALE_DAO, myServletFieldLocaleDao);
            servletContext.setAttribute(MYUSER_DAO, myUserDao);

            servletContext.setAttribute(MYLOCALE_SERVICE, myLocaleService);
            servletContext.setAttribute(SECURITY_SERVICE, securityService);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
