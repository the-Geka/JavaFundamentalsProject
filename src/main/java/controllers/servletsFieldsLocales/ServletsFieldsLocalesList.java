package controllers.servletsFieldsLocales;

import dao.interfaces.MyLocaleDao;
import dao.interfaces.MyServletDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static listeners.DaoProvider.MYLOCALE_DAO;
import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servletsFieldsLocales/")
public class ServletsFieldsLocalesList extends HttpServlet{

    private static final String VIEW = "/lib/servletsFieldsLocales/list.jsp";
    private static final String CONTROL = "/lib/servletsFieldsLocales/";

    private MyServletDao myServletDao;
    private MyLocaleDao myLocaleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("reload") != null) {
            myLocaleDao.reload();
            myServletDao.reload();
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myLocales", myLocaleDao.findAll());
        req.setAttribute("myServlets", myServletDao.findAll());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
