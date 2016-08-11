package dao.interfaces;

import model.MyUser;

import java.util.*;
import java.util.stream.Collectors;

public interface MyUserDao {

    MyUser add(MyUser myUser);
    void edit(MyUser myUser);
    void delete(long id);

    void reload();

    Collection<MyUser> findAll();

    void updateLocale(long myUserId, long myLocaleId);

    default boolean isExist(long userId) {
        return findAll().stream()
                .filter(myUser -> myUser.getId() == userId)
                .findFirst().isPresent();
    }

    default Optional<MyUser> findOne(long userId) {
        return findAll().stream()
                .filter(myUser -> myUser.getId() == userId)
                .findFirst();
    }

    default Collection<MyUser> findByStr(String str) {
        if (str.isEmpty()) return new HashSet<>();

        String lcStr = str.toLowerCase();
        return findAll().parallelStream()
                .filter(myUser -> myUser.getLastName().toLowerCase().contains(lcStr) ||
                        myUser.getFirstName().toLowerCase().contains(lcStr)).collect(Collectors.toList());
    }

    Collection<MyUser> findQueryFriends(MyUser myUser);
    Collection<MyUser> findMyQueryFriends(MyUser myUser);
    Collection<MyUser> findFriedns(MyUser myUser);

    void friendQueryAdd(MyUser myUser, long extUser);
    void friendQueryCancel(MyUser myUser, long extUser);
}
