package data;

import com.mysql.jdbc.ConnectionImpl;
import com.mysql.jdbc.JDBC4Connection;
import exceptions.DatabaseConnectionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConnectionManagerTest {
    private ConnectionManager connectionManager;
    @Mock
    private Connection connection;
    private SQLException sqlException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connectionManager = new ConnectionManager();

        sqlException = new SQLException("SQL Exception");
    }

    @Test
    public void openConnection() throws DatabaseConnectionException, SQLException {
        Connection response = connectionManager.getConnection();

        Assert.assertEquals(JDBC4Connection.class, response.getClass());
        Assert.assertEquals(true, response.getAutoCommit());

        response.close();
    }

    @Test
    public void closeConnectionDataRetrievalException() throws SQLException, DatabaseConnectionException {
        connection.setAutoCommit(false);
        doThrow(sqlException).when(connection).commit();

        SQLException sqlException = null;
        try {
            connection.commit();
            connectionManager.closeConnection(connection);
        } catch (SQLException e) {
            sqlException = e;
        }

        if (sqlException != null) {
            Assert.assertEquals(this.sqlException, sqlException);
        } else {
            Assert.fail("DatabaseConnectionException expected when calling connection.commit()");
        }

        verify(connection, times(0)).close();
    }

    @Test
    public void closeConnectionSuccessful() throws SQLException, DatabaseConnectionException {
        doNothing().when(connection).close();

        connectionManager.closeConnection(connection);

        verify(connection, times(1)).close();
    }
}
