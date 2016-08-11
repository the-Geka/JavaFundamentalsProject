package dao.mssql;

import dao.interfaces.MyServletFieldLocaleDao;
import model.MyServletFieldLocale;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class MssqlMyServletFieldLocaleDao implements MyServletFieldLocaleDao {

    private Supplier<Connection> connectionSupplier;
    private Collection<MyServletFieldLocale> myServletFieldLocaleCache;

    public MssqlMyServletFieldLocaleDao(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        reload();
    }

    private Connection getConnection() {
        return connectionSupplier.get();
    }

    @Override
    public void edit(MyServletFieldLocale myServletFieldLocale) {
        String query = "MERGE INTO ServletFieldLocale AS tgt " +
                "USING (VALUES (?,?,?)) AS src (NewServletFieldID, NewLocaleID, NewText)" +
                "ON tgt.servletFieldID = src.NewServletFieldID AND tgt.localeID = src.NewLocaleID " +
                "WHEN MATCHED THEN " +
                "UPDATE SET tgt.text = src.NewText " +
                "WHEN NOT MATCHED THEN " +
                "INSERT ([servletFieldID], [localeID], [text]) VALUES (NewServletFieldID, NewLocaleID, NewText);";


        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, myServletFieldLocale.getServletFieldId());
            ps.setLong(2, myServletFieldLocale.getLocaleId());
            ps.setString(3, myServletFieldLocale.getText());

            if (ps.executeUpdate() != 1)
                throw new SQLException("Unknown Error!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    @Override
    public synchronized void reload() {
        Collection<MyServletFieldLocale> myServletFieldLocaleCache;

        myServletFieldLocaleCache = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT servletFieldID, localeID, text FROM ServletFieldLocale ORDER BY servletFieldID")) {
            while (rs.next())
                myServletFieldLocaleCache.add(new MyServletFieldLocale(rs.getLong("servletFieldID"),
                        rs.getLong("localeID"),
                        rs.getString("text")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.myServletFieldLocaleCache = myServletFieldLocaleCache;
    }

    @Override
    public Collection<MyServletFieldLocale> findAll() {
        return myServletFieldLocaleCache;
    }
}
