package data;

import domain.PlaylistTrack;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class PlaylistTrackDaoTest {

    private PlaylistTrackDao playlistTrackDao;
    private PlaylistTrack playlistTrack;

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

        playlistTrackDao = new PlaylistTrackDao();

        playlistTrackDao.setConnectionManager(connectionManager);
        playlistTrack = new PlaylistTrack("1", 1, false, 42);

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
    public void addPlaylistTrackTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        playlistTrackDao.addPlaylistTrack(playlistTrack);

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("INSERT INTO `PlaylistTrack` (`track_id`, `playlist_id`, `offlineAvailable`, `playCount`) VALUES (? ,?, ?, ?)");
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(1)).setBoolean(anyInt(), anyBoolean());
        verify(preparedStatement, times(1)).setString(1, "1");
        verify(preparedStatement, times(1)).setInt(2, 1);
        verify(preparedStatement, times(1)).setBoolean(3, false);
        verify(preparedStatement, times(1)).setInt(4, 42);
    }

    @Test
    public void findAllTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        playlistTrackDao.findAll();

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM PlaylistTrack");
    }

    @Test
    public void findByPlaylistIdTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        playlistTrackDao.findByPlaylistId(playlistTrack.getPlaylistId());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM PlaylistTrack WHERE playlist_id = ?");
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    public void removeTrackFromPlaylistTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        playlistTrackDao.removeTrackFromPlaylist(playlistTrack.getPlaylistId(), playlistTrack.getTrackId());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("DELETE FROM `PlaylistTrack` WHERE `playlist_id` = ? AND `track_id` = ?");
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setString(2, "1");
    }
}
