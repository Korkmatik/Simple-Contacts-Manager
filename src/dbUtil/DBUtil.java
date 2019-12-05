package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_NAME = "Contacts.sqlite";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(CONNECTION_STRING);

        return connection;
    }
}
