package controllers.servlets;

import dao.interfaces.MyServletDao;
import model.MyServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servlets/add")
public class ServletsAdd extends HttpServlet {

    private static final String VIEW = "/lib/servlets/add.jsp";
    private static final String CONTROL = "/lib/servlets/";
    private MyServletDao myServletDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ofNullable(req.getParameter("name"))
                .map(name -> new MyServlet(-1, name))
                .ifPresent(myServletDao::add);

        if (req.getParameter("ok") != null)
            resp.sendRedirect(CONTROL);
        else
            req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
