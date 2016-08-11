package controllers.locales;

import dao.interfaces.MyLocaleDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_DAO;

@WebServlet("/lib/locales/delete")
public class LocalesDelete extends HttpServlet {

    private static final String VIEW = "/lib/locales/delete.jsp";
    private static final String CONTROL = "/lib/locales/";

    private MyLocaleDao myLocaleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("ok") != null) {
            ofNullable(req.getParameter("id"))
                    .map(Long::parseLong)
                    .ifPresent(myLocaleDao::delete);

            resp.sendRedirect(CONTROL);
            return;
        }

        ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(myLocaleDao::getById)
                .ifPresent(myLocale -> req.setAttribute("deletingLocale", myLocale));

        if (req.getAttribute("deletingLocale") == null)
            throw new ServletException("deletingLocale NOT FOUND");
        else
            req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

/*
        ofNullable(req.getParameter("ok"))
                .flatMap(value -> ofNullable(req.getParameter("id")))
                .map(Long::parseLong)
                .ifPresent(myLocaleDao::delete);

        if (req.getParameter("ok") != null) {
            resp.sendRedirect(CONTROL_LOCALES);
            return;
        }
 */