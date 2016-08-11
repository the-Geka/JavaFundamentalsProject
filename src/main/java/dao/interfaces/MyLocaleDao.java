package dao.interfaces;

import model.MyLocale;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public interface MyLocaleDao {

    void add(MyLocale myLocale);

    void edit(MyLocale myLocale);

    void delete(long id);

    void reload();

    default Optional<MyLocale> getById(long id) {
        if (id < 1) return Optional.empty();

        return findAll().stream()
                .filter(myLocale -> myLocale.getId() == id)
                .findFirst();
    }

    default Optional<MyLocale> getByName(String name) {
        return findAll().stream()
                .filter(myLocale -> myLocale.getName().equals(name))
                .findFirst();
    }

    default Optional<MyLocale> getByLanguage(String language) {
        return findAll().stream()
                .filter(myLocale -> myLocale.getLanguage().equals(language))
                .findFirst();
    }

    default Optional<MyLocale> getByLocale(Locale locale) {
        MyLocale findLocale = new MyLocale(-1, "",
                locale.getLanguage(),
                locale.getCountry(),
                locale.getScript(),
                locale.getVariant());

        return findAll().stream()
                .filter(myLocale -> myLocale.equals(findLocale))
                .findAny();
    }

    default boolean isExist(long localeId) {
        return findAll().stream()
                .filter(myLocale -> myLocale.getId() == localeId)
                .findFirst().isPresent();
    }

    default Optional<MyLocale> findOne(long localeId) {
        return findAll().stream()
                .filter(myLocale -> myLocale.getId() == localeId)
                .findFirst();
    }

    default Optional<Long> findOneLocaleId(long localeId) {
        return findAll().stream()
                .filter(myLocale -> myLocale.getId() == localeId)
                .findFirst().map(MyLocale::getId);
    }

    Collection<MyLocale> findAll();
}
