package org.alduthir.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDatabaseInteractionService<T> implements DatabaseInteractionInterface<T> {
    protected Connection connection;

    public AbstractDatabaseInteractionService() throws SQLException, ClassNotFoundException {
        this.connection = getConnection();
    }

    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/full-steam-drum-machine?useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root",
                    "password"
            );
        }
        return connection;
    }
}
