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

@WebServlet("/lib/servletsFields/edit")
public class ServletsFieldsEdit  extends HttpServlet {

    private static final String VIEW = "/lib/servletsFields/edit.jsp";
    private static final String CONTROL = "/lib/servletsFields/editServlet";

    private MyServletDao myServletDao;
    private MyServletFieldDao myServletFieldDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute("myServletDao");
        myServletFieldDao = (MyServletFieldDao) config.getServletContext().getAttribute("myServletFieldDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null)
            throw new ServletException("id NOT PRESENT");

        if (req.getParameter("servletId") == null)
            throw new ServletException("servletId NOT PRESENT");

        if (req.getParameter("ok") != null) {
            ofNullable(req.getParameter("id"))
                    .map(Long::parseLong)
                    .flatMap(id -> ofNullable(req.getParameter("name"))
                            .map(name -> new MyServletField(id, -1, name)))
                    .ifPresent(myServletFieldDao::edit);

            resp.sendRedirect(CONTROL + "?servletId=" + req.getParameter("servletId"));
            return;
        }

        ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(myServletFieldDao::getById)
                .ifPresent(myServletField -> req.setAttribute("editingServletField", myServletField));

        ofNullable(req.getParameter("servletId"))
                .map(Long::parseLong)
                .flatMap(myServletDao::getById)
                .ifPresent(myServlet -> req.setAttribute("myServlet", myServlet));

        if (req.getAttribute("editingServletField") == null)
            throw new ServletException("editingServletField NOT FOUND");

        if (req.getAttribute("myServlet") == null)
            throw new ServletException("myServlet NOT FOUND");

        req.getRequestDispatcher(VIEW).forward(req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
