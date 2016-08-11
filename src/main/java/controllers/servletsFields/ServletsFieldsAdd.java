package controllers.servletsFields;

import dao.interfaces.MyServletDao;
import dao.interfaces.MyServletFieldDao;
import model.MyServletField;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYSERVLETFIELD_DAO;
import static listeners.DaoProvider.MYSERVLET_DAO;

@WebServlet("/lib/servletsFields/add")
public class ServletsFieldsAdd extends HttpServlet {
    private static final String VIEW = "/lib/servletsFields/add.jsp";
    private static final String CONTROL = "/lib/servletsFields/editServlet";

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
        if (req.getParameter("servletId") == null)
            throw new ServletException("servletId NOT PRESENT");

        ofNullable(req.getParameter("servletId"))
                .map(Long::parseLong)
                .flatMap(servletId -> ofNullable(req.getParameter("name"))
                        .map(name -> new MyServletField(-1, servletId, name)))
                .ifPresent(myServletFieldDao::add);

        if (req.getParameter("ok") != null) {
            resp.sendRedirect(CONTROL + "?servletId=" + req.getParameter("servletId"));
            return;
        }
        ofNullable(req.getParameter("servletId"))
                .map(Long::parseLong)
                .flatMap(myServletDao::getById)
                .ifPresent(myServlet -> req.setAttribute("myServlet", myServlet));

        req.getRequestDispatcher(VIEW).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
