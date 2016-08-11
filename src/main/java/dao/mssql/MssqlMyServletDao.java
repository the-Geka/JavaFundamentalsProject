package dao.mssql;

import dao.interfaces.MyServletDao;
import model.MyServlet;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class MssqlMyServletDao implements MyServletDao{

    private Supplier<Connection> connectionSupplier;
    private Collection<MyServlet> myServletCache;

    public MssqlMyServletDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    public Optional<Boolean> add(String name) {
        String query = "INSERT INTO Servlet (name) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();

        return Optional.of(true);
    }

    @Override
    public void add(MyServlet myServlet) {
        String query = "INSERT INTO Servlet (name) VALUES (?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myServlet.getName());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void edit(MyServlet myServlet) {
        if (myServlet.getId() == -1) {
            add(myServlet);
            return;
        }

        String query = "UPDATE Servlet SET name = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myServlet.getName());
            ps.setLong(2, myServlet.getId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM Servlet WHERE ID = '" + id + "'";

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
        Collection<MyServlet> myServletCache;

        myServletCache = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT ID, name FROM Servlet ORDER BY name")) {
            while (rs.next())
                myServletCache.add(new MyServlet(rs.getLong("ID"),
                        rs.getString("name")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.myServletCache = myServletCache;
    }

    @Override
    public Collection<MyServlet> findAll() {
        return myServletCache;
    }
}
