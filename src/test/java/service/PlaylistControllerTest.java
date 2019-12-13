package service;

import data.interfaces.PlaylistDaoInterface;
import data.interfaces.UserDaoInterface;
import domain.User;
import dto.PlaylistRequest;
import dto.PlaylistResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.interfaces.TracksControllerInterface;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaylistControllerTest {

    private PlaylistController playlistController;
    private SQLException sqlException;
    private DatabaseQueryException databaseQueryException;
    private User user;
    private PlaylistResponse playlistResponse;
    private PlaylistRequest playlistRequest;

    @Mock
    PlaylistDaoInterface playlistDao;

    @Mock
    TracksControllerInterface tracksController;

    @Mock
    UserDaoInterface userDao;



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        playlistController = new PlaylistController();

        playlistController.setPlaylistDao(playlistDao);
        playlistController.setTracksController(tracksController);
        playlistController.setUserDao(userDao);

        sqlException = new SQLException("SQLException");
        databaseQueryException = new DatabaseQueryException(sqlException.toString(), sqlException);

        user = new User("user", "username", "password", "token");
        playlistRequest = new PlaylistRequest("name", 1);

    }

    @Test (expected = InternalServerException.class)
    public void getAllPlaylistsInternalServerError() throws DatabaseQueryException, DatabaseConnectionException,
            EntityNotFoundException, InternalServerException {
        when(playlistDao.getPlaylists()).thenThrow(databaseQueryException);

        playlistController.getAllPlaylists("token");

        verify(playlistDao, times(1)).getPlaylists();
    }

    @Test
    public void deletePlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        doNothing().when(playlistDao).deletePlaylist(any());

        playlistController.deletePlaylist(1);

        verify(playlistDao, times(1)).deletePlaylist(1);
    }

    @Test
    public void addPlaylist() throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException {
        when(userDao.getUserFromToken(any())).thenReturn(Optional.of(user));
        doNothing().when(playlistDao).addPlaylist(any(), any());

        playlistController.addPlaylist(playlistRequest, "token");

        verify(playlistDao, times(1)).addPlaylist(playlistRequest.getName(), user.getUser());
    }
}
