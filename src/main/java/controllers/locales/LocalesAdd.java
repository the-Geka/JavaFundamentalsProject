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


@WebServlet("/lib/locales/add")
public class LocalesAdd extends HttpServlet {

    private static final String VIEW = "/lib/locales/add.jsp";
    private static final String CONTROL = "/lib/locales/";
    private MyLocaleDao myLocaleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ofNullable(req.getParameter("name"))
                .flatMap(name -> ofNullable(req.getParameter("language"))
                        .flatMap(language -> ofNullable(req.getParameter("country"))
                                .flatMap(country -> ofNullable(req.getParameter("script"))
                                        .flatMap(script -> ofNullable(req.getParameter("variant"))
                                                .map(variant -> new MyLocale(-1, name, language, country, script, variant))))))
                .ifPresent(myLocaleDao::add);

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

/*
        //variant_1
        ofNullable(req.getParameter("name"))
                .flatMap(name -> ofNullable(req.getParameter("language"))
                        .flatMap(language -> ofNullable(req.getParameter("country"))
                                .flatMap(country -> ofNullable(req.getParameter("script"))
                                        .flatMap(script -> ofNullable(req.getParameter("variant"))
                                                .flatMap(variant -> myLocaleDao.add(name, language, country, script, variant))))));

        //variant_2
        ofNullable(req.getParameter("name"))
                .flatMap(name -> ofNullable(req.getParameter("language"))
                        .flatMap(language -> ofNullable(req.getParameter("country"))
                                .flatMap(country -> ofNullable(req.getParameter("script"))
                                        .flatMap(script -> ofNullable(req.getParameter("variant"))
                                                .map(variant -> new MyLocale(-1, name, language, country, script, variant))))))
                .ifPresent(myLocaleDao::add);

        //variant_3
        Optional<String> paramName = Optional.ofNullable(req.getParameter("name"));
        Optional<String> paramLanguage = Optional.ofNullable(req.getParameter("Language"));
        Optional<String> paramCounty = Optional.ofNullable(req.getParameter("County"));
        Optional<String> paramScript = Optional.ofNullable(req.getParameter("Script"));
        Optional<String> paramVariant = Optional.ofNullable(req.getParameter("Variant"));

        if (paramName.isPresent() && paramLanguage.isPresent() && paramCounty.isPresent() && paramScript.isPresent() &&
                paramVariant.isPresent())
            myLocaleDao.add(paramName.get(), paramLanguage.get(), paramCounty.get(), paramScript.get(),
                    paramVariant.get());
*/