package data;

import data.interfaces.ConnectionManagerInterface;
import exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager implements ConnectionManagerInterface {
    private Logger logger = Logger.getLogger(getClass().getName());
    private DatabaseProperties databaseProperties;

    public ConnectionManager() {
        this.databaseProperties = new DatabaseProperties();
        tryLoadJdbcDriver(databaseProperties);
    }

    private void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.getDriver(), e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        String url = databaseProperties.getUrl();
        String user = databaseProperties.getUser();
        return DriverManager.getConnection(url, user, "");
    }

    @Override
    public void closeConnection(Connection connection) throws DatabaseConnectionException {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw(new DatabaseConnectionException(e.toString(), e));
        }
    }
}
