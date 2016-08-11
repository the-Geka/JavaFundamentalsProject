package dao.interfaces;

import model.MyServletField;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MyServletFieldDao {

    void add(MyServletField myServletField);

    void edit(MyServletField myServletField);

    void delete(long id);

    void reload();

    default Optional<MyServletField> getById(long id) {
        if (id < 1) return Optional.empty();

        return findAll().stream()
                .filter(myServletField -> myServletField.getId() == id)
                .findAny();
    }

    default Optional<MyServletField> getByServletId(long id) {
        if (id < 1) return Optional.empty();

        return findAll().stream()
                .filter(myServletField -> myServletField.getServletId() == id)
                .findAny();
    }

    default long getFieldsCountByServletId(long id) {
        if (id < 1) return 0;

        return findAll().stream()
                .filter(myServletField -> myServletField.getServletId() == id)
                .count();
    }

    default Optional<Collection<MyServletField>> getAllFieldsByServletId2(long id) {
        return Optional.of(findAll().stream()
                .filter(myServletField -> myServletField.getServletId() == id)
                .collect(Collectors.toList()));
    }

    default Collection<MyServletField> getAllFieldsByServletId(long id) {
        return findAll().stream()
                .filter(myServletField -> myServletField.getServletId() == id)
                .collect(Collectors.toList());
    }

    Collection<MyServletField> findAll();

}
