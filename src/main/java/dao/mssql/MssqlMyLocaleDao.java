package dao.mssql;

import dao.interfaces.MyLocaleDao;
import model.MyLocale;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class MssqlMyLocaleDao implements MyLocaleDao {
    private Supplier<Connection> connectionSupplier;
    private Collection<MyLocale> myLocaleCache;

    public MssqlMyLocaleDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    public Optional<Boolean> add(String name, String language, String country, String script, String variant) {
        String query = "INSERT INTO Locale (name, language, country, script, variant) VALUES (?,?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, language);
            ps.setString(3, country);
            ps.setString(4, script);
            ps.setString(5, variant);

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();

        return Optional.of(true);
    }

    @Override
    public void add(MyLocale myLocale) {
        String query = "INSERT INTO Locale (name, language, country, script, variant) VALUES (?,?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myLocale.getName());
            ps.setString(2, myLocale.getLanguage());
            ps.setString(3, myLocale.getCountry());
            ps.setString(4, myLocale.getScript());
            ps.setString(5, myLocale.getVariant());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ADD ERROR");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }


    @Override
    public void edit(MyLocale myLocale) {
        if (myLocale.getId() == -1) {
            add(myLocale);
            return;
        }

        String query = "UPDATE Locale SET name = ?, language = ?, country = ?, script = ?, variant = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, myLocale.getName());
            ps.setString(2, myLocale.getLanguage());
            ps.setString(3, myLocale.getCountry());
            ps.setString(4, myLocale.getScript());
            ps.setString(5, myLocale.getVariant());
            ps.setLong(6, myLocale.getId());

            if (ps.executeUpdate() != 1)
                throw new SQLException("ID NOT FOUND");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM Locale WHERE ID = '" + id + "'";

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
        Collection<MyLocale> myLocaleCache;

        myLocaleCache = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT ID, name, language, country, script, variant FROM Locale  ORDER BY name")) {
            while (rs.next())
                myLocaleCache.add(new MyLocale(rs.getLong("ID"),
                        rs.getString("name"),
                        rs.getString("language"),
                        rs.getString("country"),
                        rs.getString("script"),
                        rs.getString("variant")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.myLocaleCache = myLocaleCache;
    }

    @Override
    public Collection<MyLocale> findAll() {
        return myLocaleCache;
    }
}
