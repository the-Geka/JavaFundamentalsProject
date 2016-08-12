package dao.mssql;

import dao.interfaces.MyMessageDao;
import model.MyMessage;
import model.MyServlet;
import model.MyUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Supplier;

public class MssqlMyMessageDao implements MyMessageDao {
    @Override
    public Collection<MyMessage> findMessageById(MyUser myUserFrom) {
        Collection<Long> longCollection = findUsersId(myUserFrom);
        Collection<MyMessage>  myMessages = new ArrayList<>();

        for (long aLong: longCollection) {
            String query = "SELECT TOP(1)* FROM [Messages] WHERE [userID_From]=? AND [userID_To]=? ORDER BY [datetime] desc";

            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, myUserFrom.getId());
                ps.setLong(2, aLong);
                ps.execute();
                ResultSet rs = ps.getResultSet();

                while (rs.next())
                    myMessages.add(
                            new MyMessage(rs.getLong(1),
                                    rs.getLong(2),
                                    rs.getTimestamp(3),
                                    rs.getString(4),
                                    rs.getBoolean(5),
                                    rs.getBoolean(6)));

                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return myMessages;
    }

    private Collection<Long> findUsersId(MyUser myUserFrom) {
        String query = "SELECT DISTINCT [userID_To] FROM [Messages] WHERE [userID_From]=?";

        Collection<Long> myUsersId = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myUserFrom.getId());
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next())
                myUsersId.add(rs.getLong(1));

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return myUsersId;
    }

    private Supplier<Connection> connectionSupplier;

    @Override
    public Collection<Integer> getPager(MyUser myUserFrom, MyUser myUserTo, int offset) {
        String query = "SELECT (COUNT(*)-1)/10+1 FROM [Messages] WHERE [userID_From]=? AND [userID_To]=?";
        Collection<Integer> integerArrayList = new ArrayList<>();

        int offsetCount = 0;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myUserFrom.getId());
            ps.setLong(2, myUserTo.getId());

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next())
                offsetCount = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (offsetCount < 6)
            for (int i = 1; i <= offsetCount; i++)
                integerArrayList.add(i);
        else {
            integerArrayList.add(1);
            if (offset == 1) {
                integerArrayList.add(2);
                integerArrayList.add(3);
                integerArrayList.add(0);
            } else if (offset == 2) {
                integerArrayList.add(2);
                integerArrayList.add(3);
                integerArrayList.add(4);
                integerArrayList.add(0);
            } else if (offset == 3) {
                integerArrayList.add(2);
                integerArrayList.add(3);
                integerArrayList.add(4);
                integerArrayList.add(5);
                integerArrayList.add(0);
            } else if (offsetCount == offset) {
                integerArrayList.add(0);
                integerArrayList.add(offsetCount - 2);
                integerArrayList.add(offsetCount - 1);
            } else if (offsetCount - 1 == offset) {
                integerArrayList.add(0);
                integerArrayList.add(offsetCount - 3);
                integerArrayList.add(offsetCount - 2);
                integerArrayList.add(offsetCount - 1);
            } else if (offsetCount - 2 == offset) {
                integerArrayList.add(0);
                integerArrayList.add(offsetCount - 4);
                integerArrayList.add(offsetCount - 3);
                integerArrayList.add(offsetCount - 2);
                integerArrayList.add(offsetCount - 1);
            } else {
                if (offset - 2 != 2) integerArrayList.add(0);

                integerArrayList.add(offset - 2);
                integerArrayList.add(offset - 1);
                integerArrayList.add(offset);
                integerArrayList.add(offset + 1);
                integerArrayList.add(offset + 2);

                if (offset + 2 != offsetCount - 1) integerArrayList.add(0);
            }
            integerArrayList.add(offsetCount);
        }

        return integerArrayList;
    }

    public MssqlMyMessageDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    @Override
    public Collection<MyMessage> findMessages(MyUser myUserFrom, MyUser myUserTo, int offset) {
        String query = "SELECT * FROM (" +
                "SELECT [datetime] ,[text], [isRead], [isFrom] FROM [Messages] " +
                "WHERE [userID_From]=? AND [userID_To]=? " +
                "ORDER BY [datetime] DESC " +
                "OFFSET ? ROWS " +
                "FETCH NEXT 10 ROWS ONLY" +
                ") AS TEMP " +
                "ORDER BY [datetime] ASC";

        Collection<MyMessage> myMessages = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myUserFrom.getId());
            ps.setLong(2, myUserTo.getId());
            ps.setInt(3, offset * 10);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next())
                myMessages.add(
                        new MyMessage(-1, -1,
                                rs.getTimestamp(1),
                                rs.getString(2),
                                rs.getBoolean(3),
                                rs.getBoolean(4)));

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        markRead(myUserFrom, myUserTo, offset);
        return myMessages;
    }

    private void markRead(MyUser myUserFrom, MyUser myUserTo, int offset) {
        String query =
                "UPDATE [Messages] SET [isRead]=1 FROM (" +
                        "SELECT [userID_From] ,[userID_To], [datetime], [text] FROM [Messages] " +
                        "WHERE [userID_From]=? AND [userID_To]=? " +
                        "ORDER BY [datetime] DESC " +
                        "OFFSET ? ROWS " +
                        "FETCH NEXT 10 ROWS ONLY" +
                        ") AS TEMP " +
                        "WHERE [Messages].[userID_From]=[TEMP].[userID_From] AND" +
                        "[Messages].[userID_To]=[TEMP].[userID_To] AND" +
                        "[Messages].[datetime]=[TEMP].[datetime] AND" +
                        "[Messages].[text]=[TEMP].[text]";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myUserFrom.getId());
            ps.setLong(2, myUserTo.getId());
            ps.setInt(3, offset * 10);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(MyMessage myMessage) {
        String query = "INSERT INTO Messages (userID_From, userID_To, datetime, text, isRead, isFrom) VALUES (?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myMessage.getUserIdFrom());
            ps.setLong(2, myMessage.getUserIdTo());
            ps.setTimestamp(3, new java.sql.Timestamp(myMessage.getDatetime().getTime()));
            ps.setString(4, myMessage.getText());
            ps.setBoolean(5, true);
            ps.setBoolean(6, true);

            ps.setLong(7, myMessage.getUserIdTo());
            ps.setLong(8, myMessage.getUserIdFrom());
            ps.setTimestamp(9, new java.sql.Timestamp(myMessage.getDatetime().getTime()));
            ps.setString(10, myMessage.getText());
            ps.setBoolean(11, false);
            ps.setBoolean(12, false);

            if (ps.executeUpdate() != 2)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
