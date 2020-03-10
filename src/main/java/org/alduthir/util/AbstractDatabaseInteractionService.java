package org.alduthir.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDatabaseInteractionService<T> implements DatabaseInteractionInterface<T> {
    protected Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/full-steam-drum-machine",
                "root",
                "password"
        );
    }
}
