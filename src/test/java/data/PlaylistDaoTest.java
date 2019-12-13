package data;

import domain.Playlist;
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
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class PlaylistDaoTest {

    private PlaylistDao playlistDao;
    private Playlist playlist;

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

        playlistDao = new PlaylistDao();

        playlistDao.setConnectionManager(connectionManager);
        playlist = new Playlist(1, "playlistName", "owner", new ArrayList<>());

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
    public void addPlaylistTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        playlistDao.addPlaylist(playlist.getName(), playlist.getOwner());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("INSERT INTO `Playlists` (`name`, `owner`) VALUE (?, ?)");
        verify(preparedStatement, times(2)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setString(1, "playlistName");
        verify(preparedStatement, times(1)).setString(2, "owner");
    }

    @Test
    public void updatePlaylistTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        playlistDao.updateName(playlist.getId(), "newName");

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("UPDATE `Playlists` SET `name` = ? WHERE `id` = ?");
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(1)).setString(1, "newName");
        verify(preparedStatement, times(1)).setInt(2, 1);
    }

    @Test
    public void deletePlaylistTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        playlistDao.deletePlaylist(playlist.getId());

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("DELETE FROM `Playlists` WHERE `id` = ?");
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    public void getAllPlaylistsTest() throws SQLException, DatabaseConnectionException, DatabaseQueryException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        playlistDao.getPlaylists();

        verify(connectionManager, times(1)).getConnection();
        verify(connectionManager, times(1)).closeConnection(connection);
        verify(connection, times(1)).prepareStatement("SELECT * FROM `Playlists`");
    }
}
