package controllers.servletsFieldsLocales;

import dao.interfaces.MyLocaleDao;
import dao.interfaces.MyServletDao;
import dao.interfaces.MyServletFieldDao;
import dao.interfaces.MyServletFieldLocaleDao;
import model.MyServletFieldLocale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.*;


@WebServlet("/lib/servletsFieldsLocales/edit")
public class ServletsFieldsLocalesEdit extends HttpServlet {

    private static final String VIEW = "/lib/servletsFieldsLocales/edit.jsp";
    private static final String CONTROL = "/lib/servletsFieldsLocales/";

    private MyLocaleDao myLocaleDao;
    private MyServletDao myServletDao;
    private MyServletFieldDao myServletFieldDao;
    private MyServletFieldLocaleDao myServletFieldLocaleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
        myServletDao = (MyServletDao) config.getServletContext().getAttribute(MYSERVLET_DAO);
        myServletFieldDao = (MyServletFieldDao) config.getServletContext().getAttribute(MYSERVLETFIELD_DAO);
        myServletFieldLocaleDao = (MyServletFieldLocaleDao) config.getServletContext().getAttribute(MYSERVLETFIELDLOCALE_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("reload") != null) {
            myLocaleDao.reload();
            myServletDao.reload();
            myServletFieldDao.reload();
            myServletFieldLocaleDao.reload();
        }

        if (req.getParameter("ok") != null) {

            if (req.getParameter("localeId") == null)
                throw new ServletException("localeId NOT PRESENT");

            long localeId = Long.parseLong(req.getParameter("localeId"));

            for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                if (entry.getKey().equals("localeId")) continue;
                if (entry.getKey().equals("ok")) continue;

                long newServletFieldId = Long.parseLong(entry.getKey());
                String newText = entry.getValue()[0];

                myServletFieldLocaleDao.edit(new MyServletFieldLocale(newServletFieldId, localeId, newText));
            }

            resp.sendRedirect(CONTROL);
            return;
        }

        if (req.getParameter("rbServletId") == null)
            throw new ServletException("rbServletId NOT PRESENT");

        if (req.getParameter("rbLocaleId") == null)
            throw new ServletException("rbLocaleId NOT PRESENT");

        ofNullable(req.getParameter("rbServletId"))
                .map(Long::parseLong)
                .flatMap(servletId -> myServletDao.getById(servletId)
                        .flatMap(myServlet -> ofNullable(myServletFieldDao.getAllFieldsByServletId(servletId))
                                .flatMap(myServletFields -> ofNullable(req.getParameter("rbLocaleId"))
                                        .map(Long::parseLong)
                                        .flatMap(localeId -> myLocaleDao.getById(localeId)
                                                .map(myLocale -> {
                                                    req.setAttribute("myLocale", myLocale);
                                                    req.setAttribute("myServlet", myServlet);
                                                    req.setAttribute("myServletFields", myServletFields);
                                                    req.setAttribute("myServletFieldLocaleDao", myServletFieldLocaleDao);
                                                    return null;
                                                })

                                        ))));

        if (req.getAttribute("myServlet") == null)
            throw new ServletException("myServlet NOT FOUND");

        if (req.getAttribute("myLocale") == null)
            throw new ServletException("myLocale NOT FOUND");

        if (req.getAttribute("myServletFields") == null)
            throw new ServletException("myServletFields NOT FOUND");

        if (req.getAttribute("myServletFieldLocaleDao") == null)
            throw new ServletException("myServletFieldLocaleDao NOT FOUND");

        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
