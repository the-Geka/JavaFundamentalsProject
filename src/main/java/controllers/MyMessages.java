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
import java.util.Optional;

import static filters.SecurityFilter.MYUSER;
import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.MYUSER_DAO;
import static listeners.DaoProvider.SECURITY_SERVICE;

@WebServlet("/myMessages")
public class MyMessages extends HttpServlet {

    private static final String NAME = "myMessages";
    private static final String VIEW = "/myMessages.jsp";
    private static final String VIEW_CHAT = "/myMessagesChat.jsp";
    private static final String CONTROL = "/myMessages";

    private MyUserDao myUserDao;
    private MyLocaleService myLocaleService;
    private SecurityService securityService;


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
        MyUser myUser = (MyUser) session.getAttribute(MYUSER);
        Optional<MyUser> myChatUser = ofNullable(req.getParameter("id"))
                .map(Long::parseLong)
                .flatMap(securityService::checkAndGetMyUser);

        if (ofNullable(req.getParameter("id")).isPresent() && !myChatUser.isPresent()) {
            resp.sendRedirect(CONTROL);
            return;
        }

        req.setAttribute("myUsersQueryFriendsSize", myUserDao.findQueryFriends(myUser).size());
        req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
        req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
        if (myChatUser.isPresent()) {
            req.setAttribute("myChatUser", myChatUser.get());
            req.getRequestDispatcher(VIEW_CHAT).forward(req, resp);
        } else
            req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
