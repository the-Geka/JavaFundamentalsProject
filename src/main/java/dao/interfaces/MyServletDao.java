package dao.interfaces;

import model.MyServlet;

import java.util.Collection;
import java.util.Optional;

public interface MyServletDao {

    void add(MyServlet myServlet);
    void edit(MyServlet myServlet);
    void delete(long id);

    void reload();

    default Optional<MyServlet> getById(long id) {
        if (id < 1) return Optional.empty();

        return findAll().stream()
                .filter(myServlet -> myServlet.getId() == id)
                .findAny();
    }

    default Optional<MyServlet> getByName(String name) {
        return findAll().stream()
                .filter(myServlet -> myServlet.getName().equals(name))
                .findFirst();
    }

    Collection<MyServlet> findAll();
}
