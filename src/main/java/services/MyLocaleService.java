package services;

import dao.interfaces.*;
import model.MyLocale;
import model.MyServlet;
import model.MyServletField;
import model.MyUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static controllers.LocaleSelector.MYLOCALE_ID;
import static filters.SecurityFilter.MYUSER;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public class MyLocaleService {

    private static final long DEFAULTLOCALEID = 69;

    private final MyLocaleDao myLocaleDao;
    private final MyServletDao myServletDao;
    private final MyServletFieldDao myServletFieldDao;
    private final MyServletFieldLocaleDao myServletFieldLocaleDao;

    public MyLocaleService(MyLocaleDao myLocaleDao, MyServletDao myServletDao, MyServletFieldDao myServletFieldDao,
                           MyServletFieldLocaleDao myServletFieldLocaleDao) {
        this.myLocaleDao = myLocaleDao;
        this.myServletDao = myServletDao;
        this.myServletFieldDao = myServletFieldDao;
        this.myServletFieldLocaleDao = myServletFieldLocaleDao;
    }

    public long getMyLocaleId(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        Optional<Long> localeIdFromSession = ofNullable((MyUser) session.getAttribute(MYUSER))
                .flatMap(myUser -> myLocaleDao.findOneLocaleId(myUser.getLocaleId())
                );
        if (localeIdFromSession.isPresent()) localeIdFromSession.get();

        Optional<Long> localeIdFromCookie = ofNullable(req.getCookies())
                .flatMap(cookies -> stream(cookies)
                        .filter(cookie -> cookie.getName().equals(MYLOCALE_ID))
                        .findFirst()
                        .map(cookie -> Long.parseLong(cookie.getValue()))
                        .flatMap(myLocaleDao::findOneLocaleId)
                );
        if (localeIdFromCookie.isPresent()) return localeIdFromCookie.get();

        return getMyLocaleIdByLocaleEnum(req.getLocales());
    }

    public MyLocaleServiceHelper getLocalizedFields(String servletName, HttpServletRequest req) {
        HttpSession session = req.getSession(true);

//        Optional<Long> localeIdFromSession = ofNullable(session.getAttribute(MYUSER))
//                .flatMap(o -> myUserDao.findOne((long) o))
//                .flatMap(myUser -> myLocaleDao.findOneLocaleId(myUser.getLocaleId())
//                );
        Optional<Long> localeIdFromSession = ofNullable((MyUser) session.getAttribute(MYUSER))
                .flatMap(myUser -> myLocaleDao.findOneLocaleId(myUser.getLocaleId())
                );
        if (localeIdFromSession.isPresent()) return getLocalizedFields(servletName, localeIdFromSession.get());

        Optional<Long> localeIdFromCookie = ofNullable(req.getCookies())
                .flatMap(cookies -> stream(cookies)
                        .filter(cookie -> cookie.getName().equals(MYLOCALE_ID))
                        .findFirst()
                        .map(cookie -> Long.parseLong(cookie.getValue()))
                        .flatMap(myLocaleDao::findOneLocaleId)
                );
        if (localeIdFromCookie.isPresent()) return getLocalizedFields(servletName, localeIdFromCookie.get());

        return getLocalizedFields(servletName, getMyLocaleIdByLocaleEnum(req.getLocales()));
    }

    private MyLocaleServiceHelper getLocalizedFields(String servletName, long localeId) {
        long servletId;
        Optional<MyServlet> myServletOptional = myServletDao.getByName(servletName);

        if (!myServletOptional.isPresent()) return new MyLocaleServiceHelper("id=?!" + servletName, localeId, new HashMap<>());
        //if (!myServletOptional.isPresent()) throw new RuntimeException("Locale NotFound for Servlet: " + servletName);
        servletId = myServletOptional.get().getId();

        Collection<MyServletField> allFieldsByServletId = myServletFieldDao.getAllFieldsByServletId(servletId);

        Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (MyServletField myServletField : allFieldsByServletId) {
            String translate = myServletFieldLocaleDao.getByServletFieldIdAndLocaleId(myServletField.getId(), localeId);

            if (translate.isEmpty()) {
                translate = myServletFieldLocaleDao.getByServletFieldIdAndLocaleId(myServletField.getId(), DEFAULTLOCALEID);
                if (translate.isEmpty())
                    translate = myServletField.getName();
            }

            map.put(myServletField.getName(), translate);
        }

        return new MyLocaleServiceHelper("id=" + servletId + "!" + servletName, localeId, map);
    }

    private long getMyLocaleIdByLocaleEnum(Enumeration<Locale> localeEnumeration) {
        List<Locale> localeList = Collections.list(localeEnumeration);

        for (Locale locale : localeList) {
            Optional<MyLocale> myLocale = myLocaleDao.getByLocale(locale);
            if (myLocale.isPresent()) return myLocale.get().getId();
        }

        for (Locale locale : localeList) {
            Optional<MyLocale> myLocale = myLocaleDao.getByLanguage(locale.getLanguage());
            if (myLocale.isPresent()) return myLocale.get().getId();
        }

        return DEFAULTLOCALEID;
    }

    public long getMyLocaleIdByLocaleId(long localeId) {
        return myLocaleDao.findAll().stream()
                .filter(myLocale -> myLocale.getId() == localeId)
                .findFirst()
                .map(MyLocale::getId)
                .orElse(DEFAULTLOCALEID);
    }
}
