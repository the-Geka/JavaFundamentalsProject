package controllers.servlets;

import javax.servlet.annotation.WebServlet;
import dao.interfaces.MyServletDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servlets/")
public class ServletsList extends HttpServlet {

    private static final String VIEW = "/lib/servlets/list.jsp";
    private static final String CONTROL = "/lib/servlets/";

    private MyServletDao myServletDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         if (req.getParameter("reload") != null) {
            myServletDao.reload();
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myServlets", myServletDao.findAll());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}