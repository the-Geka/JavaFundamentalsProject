package filters;

import core.HTTPSltRespE;
import model.MyUser;
import services.MyLocaleService;
import services.SecurityService;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.SECURITY_SERVICE;


@WebFilter("/*")
public class SecurityFilter extends HttpFilter {

    //VIEWs
    private static final String LOGINPAGE_VIEW = "/signIn.jsp";

    //ATTRIBUTEs
    public static final String MYUSER = "myUser";
    public static final String REQUESTED_URL = "requestedUrl";

    //LOCALIZATION
    private static final String LOGINPAGE_NAME = "signIn";


    private MyLocaleService myLocaleService;
    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        myLocaleService = (MyLocaleService) filterConfig.getServletContext().getAttribute(MYLOCALE_SERVICE);
        securityService = (SecurityService) filterConfig.getServletContext().getAttribute(SECURITY_SERVICE);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        final HttpSession session = req.getSession(true);

        //SignOut
        if (req.getParameter("signOut") != null) {
            session.removeAttribute(MYUSER);
            resp.sendRedirect("/");
            return;
        }


        if (req.getRequestURI().startsWith("/localeSelector") ||
                (req.getRequestURI().startsWith("/signUp") && !Optional.ofNullable(session.getAttribute(MYUSER)).isPresent())
                ) {
            chain.doFilter(req, resp);
            return;
        }
        else if (req.getRequestURI().startsWith("/signUp")){
            resp.sendError(403);
            return;
        }

        Optional<MyUser> myUserOptional = ofNullable((MyUser) session.getAttribute(MYUSER))
                .flatMap(securityService::checkAndGetMyUser);

        if (myUserOptional.isPresent()) {
            session.setAttribute(MYUSER, myUserOptional.get());
            chain.doFilter(req, resp);
        } else {
            setJID4IE(session, resp);

            myUserOptional = ofNullable(req.getParameter("j_username"))
                    .flatMap(email -> ofNullable(req.getParameter("j_password"))
                            .flatMap(password -> securityService.checkAndGetMyUser(email, password))
                    );
            if (myUserOptional.isPresent()) {
                session.setAttribute(MYUSER, myUserOptional.get());
                chain.doFilter(req, resp);
            } else {
                String userMessage;
                if (ofNullable(req.getParameter("j_username")).isPresent() ||
                        ofNullable(req.getParameter("j_password")).isPresent())
                    userMessage = myLocaleService.getLocalizedFields(LOGINPAGE_NAME, req).get("ErrorSignInMSG");
                else
                    userMessage = myLocaleService.getLocalizedFields(LOGINPAGE_NAME, req).get("WelcomeMSG");

                req.setAttribute("actionStatus", userMessage);
                req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
                req.setAttribute("l", myLocaleService.getLocalizedFields(LOGINPAGE_NAME, req));
                req.getRequestDispatcher(LOGINPAGE_VIEW).forward(req, resp);
            }
        }
    }

    private void setJID4IE(HttpSession session, HttpServletResponse resp) {
        Cookie cJID = new Cookie("JSESSIONID", session.getId());
        cJID.setMaxAge(3600 * 24);
        new HTTPSltRespE(resp).addCookie(cJID);
    }
}
