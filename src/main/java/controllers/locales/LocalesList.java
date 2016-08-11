package controllers.locales;

import dao.interfaces.MyLocaleDao;
import services.MyLocaleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.MYLOCALE_DAO;

@WebServlet("/lib/locales/")
public class LocalesList extends HttpServlet {

    private static final String NAME = "locales";
    private static final String VIEW = "/lib/locales/list.jsp";
    private static final String CONTROL= "/lib/locales/";

    private MyLocaleDao myLocaleDao;
    private MyLocaleService myLocaleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("reload") != null) {
            myLocaleDao.reload();
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myLocales", myLocaleDao.findAll());

        req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
