package controllers.servlets;

import dao.interfaces.MyServletDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servlets/delete")
public class ServletsDelete extends HttpServlet {

    private static final String VIEW = "/lib/servlets/delete.jsp";
    private static final String CONTROL = "/lib/servlets/";

    private MyServletDao myServletDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("ok") != null) {
            ofNullable(req.getParameter("id"))
                    .map(Long::parseLong)
                    .ifPresent(myServletDao::delete);

            resp.sendRedirect(CONTROL);
            return;
        }

        ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(myServletDao::getById)
                .ifPresent(myServlet -> req.setAttribute("deletingServlet", myServlet));

        if (req.getAttribute("deletingServlet") == null)
            throw new ServletException("deletingServlet NOT FOUND");
        else
            req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
