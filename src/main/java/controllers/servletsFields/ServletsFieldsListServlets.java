package controllers.servletsFields;

import dao.interfaces.MyServletDao;
import dao.interfaces.MyServletFieldDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static listeners.DaoProvider.MYSERVLETFIELD_DAO;
import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servletsFields/")
public class ServletsFieldsListServlets extends HttpServlet {

    private static final String VIEW = "/lib/servletsFields/list.jsp";
    private static final String CONTROL = "/lib/servletsFields/";

    private MyServletDao myServletDao;
    private MyServletFieldDao myServletFieldDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
        myServletFieldDao = (MyServletFieldDao) config.getServletContext().getAttribute(MYSERVLETFIELD_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("reload") != null) {
            myServletDao.reload();
            myServletFieldDao.reload();
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myServlets", myServletDao.findAll());
        req.setAttribute("myServletFieldDao", myServletFieldDao);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
