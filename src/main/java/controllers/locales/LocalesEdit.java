package controllers.locales;

import dao.interfaces.MyLocaleDao;
import model.MyLocale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_DAO;


@WebServlet("/lib/locales/edit")
public class LocalesEdit extends HttpServlet {

    private static final String VIEW = "/lib/locales/edit.jsp";
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
                    .flatMap(id -> ofNullable(req.getParameter("name"))
                            .flatMap(name -> ofNullable(req.getParameter("language"))
                                    .flatMap(language -> ofNullable(req.getParameter("country"))
                                            .flatMap(country -> ofNullable(req.getParameter("script"))
                                                    .flatMap(script -> ofNullable(req.getParameter("variant"))
                                                            .map(variant -> new MyLocale(id, name, language, country, script, variant)))))))
                    .ifPresent(myLocaleDao::edit);

            resp.sendRedirect(CONTROL);
            return;
        }

        ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(myLocaleDao::getById)
                .ifPresent(myLocale -> req.setAttribute("editingLocale", myLocale));

        if (req.getAttribute("editingLocale") == null)
            throw new ServletException("editingLocale NOT FOUND");
        else
            req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
