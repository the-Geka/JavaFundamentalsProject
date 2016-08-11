package controllers;

import dao.interfaces.MyUserDao;
import model.MyUser;
import services.MyLocaleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static filters.SecurityFilter.MYUSER;
import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.MYUSER_DAO;

@WebServlet("/myFriends")
public class UserFriends extends HttpServlet {


    private static final String NAME = "userFriends";
    private static final String VIEW = "/userFriends.jsp";
    private static final String CONTROL = "/myFriends";

    private MyLocaleService myLocaleService;
    private MyUserDao myUserDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
        myUserDao = (MyUserDao) config.getServletContext().getAttribute(MYUSER_DAO);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        MyUser myUser = (MyUser) session.getAttribute(MYUSER);

        Collection<MyUser> myUsersFindResult = ofNullable(req.getParameter("inputFindStr"))
                .map(myUserDao::findByStr).orElse(Collections.emptyList());

        if (req.getParameter("add") != null) {
            ofNullable(req.getParameter("id"))
                    .map(Long::parseLong)
                    .ifPresent(aLong -> myUserDao.friendQueryAdd(myUser, aLong));
            resp.sendRedirect(CONTROL);
            return;
        }

        if (req.getParameter("cancel") != null) {
            ofNullable(req.getParameter("id"))
                    .map(Long::parseLong)
                    .ifPresent(aLong -> myUserDao.friendQueryCancel(myUser, aLong));
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myUsersFindResult", myUsersFindResult);
        req.setAttribute("myUsersQueryFriends", myUserDao.findQueryFriends(myUser));
        req.setAttribute("myUsersFriends", myUserDao.findFriedns(myUser));
        req.setAttribute("myUsersMyQueryFriends", myUserDao.findMyQueryFriends(myUser));


        req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
        req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
