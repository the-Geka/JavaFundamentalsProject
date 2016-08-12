package controllers;

import dao.interfaces.MyMessageDao;
import dao.interfaces.MyUserDao;
import model.MyMessage;
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
import java.util.Date;
import java.util.Optional;

import static filters.SecurityFilter.MYUSER;
import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.*;

@WebServlet("/myMessages")
public class MyMessages extends HttpServlet {

    private static final String NAME = "myMessages";
    private static final String VIEW = "/myMessages.jsp";
    private static final String VIEW_CHAT = "/myMessagesChat.jsp";
    private static final String CONTROL = "/myMessages";

    private MyUserDao myUserDao;
    private MyLocaleService myLocaleService;
    private MyMessageDao myMessageDao;
    private SecurityService securityService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
        myUserDao = (MyUserDao) config.getServletContext().getAttribute(MYUSER_DAO);
        myMessageDao = (MyMessageDao) config.getServletContext().getAttribute(MYMESSAGE_DAO);
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

        req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
        req.setAttribute("myUsersQueryFriendsSize", myUserDao.findQueryFriends(myUser).size());
        req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
        if (myChatUser.isPresent()) {
            //req.setAttribute(REQUESTED_URL, req.getRequestURI() + "?id=" + myChatUser.get().getId());
            ofNullable(req.getParameter("msg"))
                    .map(name -> new MyMessage(myUser.getId(), myChatUser.get().getId(), new Date(), name, true, true))
                    .ifPresent(myMessageDao::add);

            int offset = ofNullable(req.getParameter("offset"))
                    .map(Integer::parseInt).orElse(1);

            req.setAttribute("myMessages", myMessageDao.findMessages(myUser, myChatUser.get(), offset - 1));
            req.setAttribute("pagger", myMessageDao.getPager(myUser, myChatUser.get(), offset));


            req.setAttribute("myChatUser", myChatUser.get());
            req.getRequestDispatcher(VIEW_CHAT).forward(req, resp);
        } else {

            req.setAttribute("securityService", securityService);
            req.setAttribute("myChatsUsers", myMessageDao.findMessageById(myUser));

            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
