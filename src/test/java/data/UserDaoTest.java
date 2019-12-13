package data;

import data.interfaces.ConnectionManagerInterface;
import domain.User;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.DatabaseUpdateException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDaoTest {

    private UserDao userDao;
    private User user;
    private User invalidUser;
    private ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException;

    @Mock
    private ConnectionManagerInterface connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws DatabaseConnectionException, SQLException {
        MockitoAnnotations.initMocks(this);

        userDao = new UserDao();

        userDao.setConnectionManager(connectionManager);
        user = new User("test", "testPersoon", "testpw", "1234");
        invalidUser = new User(null, null, null, null);
        arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();

        when(connectionManager.getConnection()).thenReturn(connection);
        doNothing().when(connectionManager).closeConnection(any());
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
    }

    @Test
    public void getUserTest() throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException, SQLException {
        userDao.getUser(user.getUser());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM Users where user = ?");
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setString(1, "test");
    }

    @Test
    public void getUserFromTokenTest() throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException, SQLException {
        userDao.getUserFromToken(user.getToken());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM Users where token = ?");
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setString(1, "1234");
    }

    @Test
    public void updateUserTest() throws DatabaseConnectionException, SQLException, DatabaseUpdateException, InvalidEntityException {
        userDao.updateUser(user.getUser(), user);

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("UPDATE users SET username = ?, password = ?, user = ?, token = ? WHERE user = ?");
        verify(preparedStatement, times(5)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setString(1, "testPersoon");
        verify(preparedStatement, times(1)).setString(2, "testpw");
        verify(preparedStatement, times(1)).setString(3, "test");
        verify(preparedStatement, times(1)).setString(4, "1234");
        verify(preparedStatement, times(1)).setString(5, "test");
    }

    @Test (expected = InvalidEntityException.class)
    public void updateInvalidUserTest() throws DatabaseConnectionException, DatabaseUpdateException, InvalidEntityException {
        userDao.updateUser(user.getUser(), invalidUser);
    }


    @Test
    public void getTokens() throws DatabaseQueryException, DatabaseConnectionException, SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        userDao.getTokens();

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT token FROM users");
    }

    @Test
    public void createDatabaseConnectionException() throws DatabaseUpdateException, InvalidEntityException, SQLException, DatabaseConnectionException {
        when(connectionManager.getConnection()).thenThrow(arrayIndexOutOfBoundsException);

        ArrayIndexOutOfBoundsException thrownDatabaseConnectionException = null;
        try {
            userDao.updateUser(user.getUser(), user);
        } catch (ArrayIndexOutOfBoundsException e) {
            thrownDatabaseConnectionException = e;
        }

        if (thrownDatabaseConnectionException != null) {
            Assert.assertEquals(arrayIndexOutOfBoundsException, thrownDatabaseConnectionException);
        } else {
            Assert.fail("DatabaseConnectionException expected when calling connectionManager.openConnection()");
        }

        verify(connectionManager, times(1)).getConnection();
    }

}
