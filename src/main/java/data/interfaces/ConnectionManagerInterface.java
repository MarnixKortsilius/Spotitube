package data.interfaces;

import exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManagerInterface {
    Connection getConnection() throws SQLException;
    void closeConnection(Connection connection) throws DatabaseConnectionException;
}
