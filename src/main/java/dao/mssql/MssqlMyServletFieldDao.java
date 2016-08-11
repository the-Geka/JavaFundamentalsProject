package dao.mssql;

import dao.interfaces.MyServletFieldDao;
import model.MyServletField;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class MssqlMyServletFieldDao implements MyServletFieldDao{

    private Supplier<Connection> connectionSupplier;
    private Collection<MyServletField> myServletFieldCache;

    public MssqlMyServletFieldDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    @Override
    public void add(MyServletField myServletField) {
        String query = "INSERT INTO ServletField (servletID, name) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myServletField.getServletId());
            ps.setString(2, myServletField.getName());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void edit(MyServletField myServletField) {
        if (myServletField.getId() == -1) {
            add(myServletField);
            return;
        }

        String query = "UPDATE ServletField SET name = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myServletField.getName());
            ps.setLong(2, myServletField.getId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM ServletField WHERE ID = '" + id + "'";

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
        Collection<MyServletField> myServletFieldCache;

        myServletFieldCache = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT ID, servletID, name FROM ServletField ORDER BY servletID")) {
            while (rs.next())
                myServletFieldCache.add(new MyServletField(rs.getLong("ID"),
                        rs.getLong("servletID"),
                        rs.getString("name")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.myServletFieldCache = myServletFieldCache;
    }

    @Override
    public Collection<MyServletField> findAll() {
        return myServletFieldCache;
    }
}
