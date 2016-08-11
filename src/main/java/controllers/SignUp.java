package controllers;

import model.MyUser;
import services.MyLocaleService;
import services.MyLocaleServiceHelper;
import services.SecurityService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static filters.SecurityFilter.MYUSER;
import static filters.SecurityFilter.REQUESTED_URL;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static listeners.DaoProvider.MYLOCALE_SERVICE;
import static listeners.DaoProvider.SECURITY_SERVICE;

@WebServlet("/signUp")
public class SignUp extends HttpServlet {

    private static final String NAME = "signUp";
    private static final String VIEW = "/signUp.jsp";

    private MyLocaleService myLocaleService;
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLocaleService = (MyLocaleService) config.getServletContext().getAttribute(MYLOCALE_SERVICE);
        securityService = (SecurityService) config.getServletContext().getAttribute(SECURITY_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<MyUser> myUserOptional = validateAndGetNewUser(req);
        HttpSession session = req.getSession(true);

        if (myUserOptional.isPresent()) {
            session.setAttribute(MYUSER, myUserOptional.get());
            req.getRequestDispatcher(
                    ofNullable(req.getParameter(REQUESTED_URL)).orElse("/"))
                    .forward(req, resp);
        } else {
            req.setAttribute(REQUESTED_URL, ofNullable(req.getParameter(REQUESTED_URL)).orElse("/"));
            //req.setAttribute(REQUESTED_URL, req.getRequestURI() + ofNullable(req.getQueryString()).map(s -> "?" + s).orElse(""));
            req.setAttribute("l", myLocaleService.getLocalizedFields(NAME, req));
            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private Optional<MyUser> validateAndGetNewUser(HttpServletRequest req) {
        Optional<MyUser> myUserOptional = Optional.empty();
        MyLocaleServiceHelper myLocaleServiceHelper = myLocaleService.getLocalizedFields(NAME, req);
        String actionStatus = "";
        //// TODO: 10.08.2016 сделать стрингбилдером

        String uiEmail = req.getParameter("email");
        String uiPwd = req.getParameter("password");
        String uiPwdConf = req.getParameter("passwordConf");
        String uiLName = req.getParameter("lastName");
        String uiFName = req.getParameter("firstName");

        if (uiEmail == null && uiPwd == null && uiPwdConf == null && uiLName == null && uiFName == null)
            actionStatus = myLocaleServiceHelper.get("WelcomeMSG");
        else if (uiPwdConf == null)
            actionStatus = myLocaleServiceHelper.get("Error. PasswordConf. Not present");
        else if (uiPwd == null)
            actionStatus = myLocaleServiceHelper.get("Error. Password. Not present");
        else if (!uiPwd.equals(uiPwdConf))
            actionStatus = myLocaleServiceHelper.get("Error. Password != PasswordConf");
        else {
            // TODO: 10.08.2016 Если чел накосячил, сохранить все поля кроме пароля
            MyUser newMyUser = new MyUser(-1, uiEmail, uiPwd, uiLName, uiFName, myLocaleService.getMyLocaleId(req));

            ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
            Validator validator = vf.getValidator();
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(newMyUser);

            for (ConstraintViolation<Object> cv : constraintViolations)
                actionStatus += cv.getMessage() + ". ";

            if (constraintViolations.size() == 0)
                if (securityService.checkMyUserIsExist(uiEmail))
                    actionStatus += "Error. User is already exist";
                else
                    myUserOptional = of(securityService.addNewMyUser(newMyUser));
        }

        req.setAttribute("actionStatus",actionStatus);

        return myUserOptional;
    }


//    public static void validate222(Object object, Validator validator) {
//        Set<ConstraintViolation<Object>> constraintViolations = validator
//                .validate(object);
//
//        System.out.println(object);
//        System.out.println(String.format("Кол-во ошибок: %d",
//                constraintViolations.size()));
//
//        for (ConstraintViolation<Object> cv : constraintViolations)
//            System.out.println(String.format(
//                    "Внимание, ошибка! property: [%s], value: [%s], message: [%s]",
//                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));
//    }
}
