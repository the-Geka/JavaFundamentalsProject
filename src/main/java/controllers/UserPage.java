package controllers;

import dao.interfaces.MyUserDao;
import model.MyUser;
import services.MyLocaleService;
import services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static filters.SecurityFilter.MYUSER;
import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.MYUSER_DAO;
import static listeners.DaoProvider.SECURITY_SERVICE;

@WebServlet("")
public class UserPage extends HttpServlet {

    private static final String NAME = "userPage";
    private static final String VIEW = "/userPage.jsp";
    private static final String CONTROL = "/";

    private MyLocaleService myLocaleService;
    private SecurityService securityService;
    private MyUserDao myUserDao;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
        myUserDao = (MyUserDao) config.getServletContext().getAttribute(MYUSER_DAO);
        securityService = (SecurityService) config.getServletContext().getAttribute(SECURITY_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        MyUser myCurrentUser = (MyUser) session.getAttribute(MYUSER);

        MyUser myUser = ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(securityService::checkAndGetMyUser)
                .orElse(myCurrentUser);

        boolean itsMe = myCurrentUser.getId() == myUser.getId();
        req.setAttribute("itsMe", itsMe);

        if (itsMe && req.getParameter("id") != null) {
            resp.sendRedirect(CONTROL);
            return;
        }


        req.setAttribute("myUser", myUser);

        req.setAttribute("myUsersQueryFriends", myUserDao.findQueryFriends(myCurrentUser));
        req.setAttribute("myUsersFriends", myUserDao.findFriedns(myCurrentUser));
        req.setAttribute("myUsersMyQueryFriends", myUserDao.findMyQueryFriends(myCurrentUser));

        req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
        req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
