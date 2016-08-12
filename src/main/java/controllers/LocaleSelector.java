package controllers;

import core.HTTPSltRespE;
import model.MyUser;
import services.MyLocaleService;
import dao.interfaces.MyLocaleDao;
import dao.interfaces.MyUserDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.Optional;

import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.ofNullable;

import static filters.SecurityFilter.MYUSER;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.MYLOCALE_DAO;
import static listeners.DaoProvider.MYUSER_DAO;

@WebServlet("/localeSelector")
public class LocaleSelector extends HttpServlet {

    private static final String VIEW = "/lib/localeSelector.jsp";
    private static final String CONROL = "/";

    public static final String MYLOCALE_ID = "localeId";


    private MyLocaleDao myLocaleDao;
    private MyUserDao myUserDao;
    private MyLocaleService myLocaleService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleDao = (MyLocaleDao) config.getServletContext().getAttribute(MYLOCALE_DAO);
        myUserDao = (MyUserDao) config.getServletContext().getAttribute(MYUSER_DAO);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter(MYLOCALE_ID) != null) {
            long newMyLocaleId;

            try {
                newMyLocaleId = Long.parseLong(req.getParameter(MYLOCALE_ID));
            } catch (NumberFormatException e) {
                throw new ServletException(e);
            }

            //VALIDATE = CHECK OR DEFAULT
            newMyLocaleId = myLocaleService.getMyLocaleIdByLocaleId(newMyLocaleId);

            Cookie cookieMyLocaleId = new Cookie(MYLOCALE_ID, Long.toString(newMyLocaleId));
            //cookieMyLocaleId.setPath("/");

            HttpSession session = req.getSession(true);

            Optional<MyUser> myUserOptional = ofNullable((MyUser) session.getAttribute(MYUSER));
            if (myUserOptional.isPresent()) {
                MyUser myUser = myUserOptional.get();
                MyUser newMyUser = new MyUser(myUser.getId(), myUser.getEmail(), myUser.getPassword(),
                        myUser.getLastName(), myUser.getFirstName(), newMyLocaleId);

                myUserDao.updateLocale(myUser.getId(), newMyUser.getLocaleId());
                session.setAttribute(MYUSER, newMyUser);
            } else
                cookieMyLocaleId.setMaxAge(3600 * 24 * 365);

            new HTTPSltRespE(resp).addCookie(cookieMyLocaleId);
            //resp.addCookie(cookieMyLocaleId);

            if (req.getParameter(REQUESTED_URL) != null)
                resp.sendRedirect(req.getParameter(REQUESTED_URL));
            else
                resp.sendRedirect(CONROL);

            return;
        }

        req.setAttribute(REQUESTED_URL, ofNullable(req.getParameter(REQUESTED_URL)).orElse("/"));

        req.setAttribute("myLocales", myLocaleDao.findAll());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
