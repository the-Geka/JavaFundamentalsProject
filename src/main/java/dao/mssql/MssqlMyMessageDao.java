package dao.mssql;

import dao.interfaces.MyMessageDao;
import model.MyMessage;
import model.MyServlet;
import model.MyUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Supplier;

public class MssqlMyMessageDao implements MyMessageDao {

    private Supplier<Connection> connectionSupplier;
    private Collection<MyServlet> myServletCache;

    public MssqlMyMessageDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        //reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    @Override
    public Collection<MyMessage> findMessages(MyUser myUserFrom, MyUser myUserTo, int offset) {
        return null;
    }

    @Override
    public void add(MyMessage myMessage) {
        String query = "INSERT INTO Messages (userID_From, userID_To, datetime, text, isRead) VALUES (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myMessage.getUserIdFrom());
            ps.setLong(2, myMessage.getUserIdTo());
            ps.setTimestamp(3, new java.sql.Timestamp(myMessage.getDatetime().getTime()));
            ps.setString(4, myMessage.getText());
            ps.setBoolean(5, true);

            ps.setLong(6, myMessage.getUserIdTo());
            ps.setLong(7, myMessage.getUserIdFrom());
            ps.setTimestamp(8, new java.sql.Timestamp(myMessage.getDatetime().getTime()));
            ps.setString(9, myMessage.getText());
            ps.setBoolean(10, false);

            if (ps.executeUpdate() != 2)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
