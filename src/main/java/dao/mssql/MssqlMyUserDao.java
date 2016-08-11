package dao.mssql;

import dao.interfaces.MyUserDao;
import model.MyUser;

import java.lang.annotation.Documented;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MssqlMyUserDao implements MyUserDao {

    private Supplier<Connection> connectionSupplier;
    private Collection<MyUser> myUserCache;

    public MssqlMyUserDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    public void addVoid(MyUser myUser) {
        String query = "INSERT INTO Users (email, password, lastName, firstName, localeID) VALUES (?,?,?,?,?)";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myUser.getEmail());
            ps.setString(2, myUser.getPassword());
            ps.setString(3, myUser.getLastName());
            ps.setString(4, myUser.getFirstName());
            ps.setLong(5, myUser.getLocaleId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public MyUser add(MyUser myUser) {
        String query = "INSERT INTO Users (email, password, lastName, firstName, localeID) VALUES (?,?,?,?,?)";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myUser.getEmail());
            ps.setString(2, myUser.getPassword());
            ps.setString(3, myUser.getLastName());
            ps.setString(4, myUser.getFirstName());
            ps.setLong(5, myUser.getLocaleId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
        //noinspection OptionalGetWithoutIsPresent
        return findAll().stream().filter(myUser1 -> myUser1.getEmail().equals(myUser.getEmail())).findFirst().get();
    }

    @Override
    public void edit(MyUser myUser) {
        if (myUser.getId() == -1) {
            add(myUser);
            return;
        }

        String query = "UPDATE Users SET email = ?, password = ?, lastName = ?, firstName = ?, localeID = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myUser.getEmail());
            ps.setString(2, myUser.getPassword());
            ps.setString(3, myUser.getLastName());
            ps.setString(4, myUser.getFirstName());
            ps.setLong(5, myUser.getLocaleId());
            ps.setLong(6, myUser.getId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM Users WHERE ID = '" + id + "'";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            if (statement.executeUpdate(query) != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public synchronized void reload() {
        Collection<MyUser> myUserCache;

        myUserCache = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT ID, email, password, lastName, firstName, localeID FROM Users ORDER BY email")) {
            while (rs.next())
                myUserCache.add(new MyUser(rs.getLong("ID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getLong("localeID")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.myUserCache = myUserCache;
    }

    @Override
    public Collection<MyUser> findAll() {
        return myUserCache;
    }

    @Override
    public void updateLocale(long myUserId, long myLocaleId) {
        String query = "UPDATE Users SET localeID = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myLocaleId);
            ps.setLong(2, myUserId);

            if (ps.executeUpdate() != 1) throw new SQLException("myUserId NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public Collection<MyUser> findFriedns(MyUser myUser) {
        String query = "{call dbo.FindFriends(?)}";
        Collection<MyUser> myUsers = new HashSet<>();

        try (Connection connection = getConnection();
             CallableStatement cstmt = connection.prepareCall(query)) {
            //cstmt.setLong("UserID", myUser.getId());
            cstmt.setLong(1, myUser.getId());

            cstmt.execute();

            ResultSet rs = cstmt.getResultSet();
            while (rs.next())
                myUsers.add(findOne(
                        rs.getLong("userID_right")).orElse(new MyUser(-1, "Deleted", "Deleted", "Deleted", "Deleted", -1)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return myUsers;
    }

    @Override
    public Collection<MyUser> findQueryFriends(MyUser myUser) {
        return findQuery(myUser, "{call dbo.FindQueryFriends(?)}");
    }

    @Override
    public Collection<MyUser> findMyQueryFriends(MyUser myUser) {
        return findQuery(myUser, "{call dbo.FindMyQueryFriends(?)}");
    }

    private Collection<MyUser> findQuery(MyUser myUser, String query) {
        Collection<MyUser> myUsers = new HashSet<>();

        try (Connection connection = getConnection();
             CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setLong(1, myUser.getId());

            cstmt.execute();

            ResultSet rs = cstmt.getResultSet();
            while (rs.next())
                myUsers.add(findOne(rs.getLong(1)).orElse(new MyUser(-1, "Deleted", "Deleted", "Deleted", "Deleted", -1)));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return myUsers;
    }

    public void friendQueryAdd(MyUser myUser, long extUser) {
        String query = "INSERT INTO Friends (userID_left, userID_right) VALUES (?,?)";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myUser.getId());
            ps.setLong(2, extUser);

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void friendQueryCancel(MyUser myUser, long extUser) {
        String query = "DELETE FROM Friends WHERE userID_left = '" + myUser.getId() + "'" + " AND userID_right = '" + extUser + "'";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            if (statement.executeUpdate(query) != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void friendQueryAccept(MyUser myUser, long extUser) {
        friendQueryAdd(new MyUser(extUser, "", "", "", "", -1), myUser.getId());
    }

}
