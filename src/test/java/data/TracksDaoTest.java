package data;

import domain.PlaylistTrack;
import domain.Track;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TracksDaoTest {
    private TracksDao tracksDao;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws DatabaseConnectionException, SQLException {
        MockitoAnnotations.initMocks(this);

        tracksDao = new TracksDao();

        tracksDao.setConnectionManager(connectionManager);

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
    public void findAllTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        tracksDao.findAll();

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM Tracks");
    }
}
