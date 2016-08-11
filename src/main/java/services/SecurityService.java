package services;

import dao.interfaces.MyUserDao;
import model.MyUser;

import java.security.MessageDigest;
import java.util.Optional;

public class SecurityService {

    private final MyUserDao myUserDao;

    public SecurityService(MyUserDao myUserDao) {
        this.myUserDao = myUserDao;
    }

    public MyUser addNewMyUser(MyUser myUser) {
        return myUserDao.add(myUser);
    }

    public boolean checkMyUserIsExist(String email) {
        return myUserDao.findAll().stream()
                .filter(myUser ->
                        myUser.getEmail().equals(email)
                )
                .findFirst().isPresent();
    }

    public Optional<MyUser> checkAndGetMyUser(String email, String password) {
        return myUserDao.findAll().stream()
                .filter(myUser ->
                        myUser.getEmail().equals(email) && myUser.getPassword().equals(password)
                )
                .findFirst();
    }

    public Optional<MyUser> checkAndGetMyUser(long id) {
        return checkAndGetMyUser(new MyUser(id, null, null, null, null, -1));
    }

    public Optional<MyUser> checkAndGetMyUser(MyUser myUserToCheck) {
        return myUserDao.findAll().stream()
                .filter(myUser ->
                        myUser.getId() == myUserToCheck.getId()
                )
                .findFirst();
    }

//    private static final MessageDigest md5;

}
