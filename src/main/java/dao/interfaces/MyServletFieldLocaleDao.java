package dao.interfaces;

import model.MyServletFieldLocale;

import java.util.Collection;

public interface MyServletFieldLocaleDao {

    void edit(MyServletFieldLocale myServletFieldLocale);
    void reload();

    Collection<MyServletFieldLocale> findAll();

    default String getByServletFieldIdAndLocaleId(long servletFieldId, long localeId) {
        return findAll().stream()
                .filter(myServletFieldLocale -> myServletFieldLocale.getLocaleId() == localeId
                        && myServletFieldLocale.getServletFieldId() == servletFieldId)
                .findFirst()
                .orElseGet(() -> new MyServletFieldLocale(-1, -1, ""))
                .getText();
    }

}
