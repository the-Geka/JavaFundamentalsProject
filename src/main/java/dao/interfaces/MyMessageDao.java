package dao.interfaces;

import model.MyMessage;
import model.MyUser;

import java.util.Collection;

public interface MyMessageDao {
    void add(MyMessage myMessage);
    Collection<MyMessage> findMessages(MyUser myUserFrom, MyUser myUserTo, int offset);
    Collection<MyMessage> findMessageById(MyUser myUserFrom);
    Collection<Integer> getPager(MyUser myUserFrom, MyUser myUserTo, int offset);
}
