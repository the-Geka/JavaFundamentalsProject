package testers;

import javax.servlet.annotation.WebServlet;

import dao.interfaces.MyLocaleDao;
import model.MyLocale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;

@WebServlet("/t/locales")
public class LocalesServlet extends HttpServlet {

    private MyLocaleDao myLocaleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute("myLocaleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ofNullable(req.getParameter("reload"))
                .ifPresent(value -> myLocaleDao.reload());

        ofNullable(req.getParameter("update"))
                .flatMap(value -> ofNullable(req.getParameter("id")))
                .map(Long::parseLong)
                .flatMap(id -> ofNullable(req.getParameter("name"))
                        .flatMap(name -> ofNullable(req.getParameter("language"))
                                .flatMap(language -> ofNullable(req.getParameter("country"))
                                        .flatMap(country -> ofNullable(req.getParameter("script"))
                                                .flatMap(script -> ofNullable(req.getParameter("variant"))
                                                        .map(variant -> new MyLocale(id, name, language, country, script, variant)))))))
                .ifPresent(myLocaleDao::edit);

        ofNullable(req.getParameter("delete"))
                .flatMap(value -> ofNullable(req.getParameter("id")))
                .map(Long::parseLong)
                .ifPresent(myLocaleDao::delete);

        MyLocale currentEditedLocale = ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(id -> myLocaleDao.getById(id)).orElse(new MyLocale(-1, "", "", "", "", ""));

        req.setAttribute("currentEditedLocale", currentEditedLocale);
        req.setAttribute("myLocales", myLocaleDao.findAll());

        req.getRequestDispatcher("LocalesView.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
