package presentation;

import dto.PlaylistRequest;
import dto.PlaylistResponse;
import dto.TrackRequest;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidEntityException;
import exceptions.InvalidLoginException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.interfaces.PlaylistControllerInterface;
import service.interfaces.PlaylistTrackControllerInterface;
import service.interfaces.TracksControllerInterface;

import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaylistEndpointTest {

    PlaylistEndpoint playlistEndpoint;
    @Mock
    PlaylistControllerInterface playlistController;
    @Mock
    TracksControllerInterface tracksController;
    @Mock
    PlaylistTrackControllerInterface playlistTrackController;

    PlaylistRequest playlistRequest;
    PlaylistResponse playlistResponse;

    TrackResponse trackResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        playlistEndpoint = new PlaylistEndpoint();
        playlistEndpoint.setPlaylistController(playlistController);
        playlistEndpoint.setTracksController(tracksController);
        playlistEndpoint.setPlaylistTrackController(playlistTrackController);

        playlistRequest = new PlaylistRequest("name", 1);
        playlistRequest.setId(1);
        playlistRequest.setName("test");
        playlistResponse = new PlaylistResponse();
        trackResponse = new TrackResponse();
    }

    @Test
    public void getAllPlaylistsInternalServerException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new InternalServerException());

        Response response = playlistEndpoint.getPlaylists("token");

        Assert.assertEquals(500, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void getAllPlaylistsEntityNotFoundException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new EntityNotFoundException());

        Response response = playlistEndpoint.getPlaylists("token");

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void getAllPlayListTest() throws EntityNotFoundException, InternalServerException, InvalidEntityException,
            InvalidLoginException {
        when(playlistController.getAllPlaylists(any())).thenReturn(playlistResponse);

        Response response = playlistEndpoint.getPlaylists("token");

        Assert.assertEquals(response.toString(), playlistResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void deletePlaylistsInternalServerException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new InternalServerException());

        Response response = playlistEndpoint.deletePlaylist(1,"token");

        Assert.assertEquals(500, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void deletePlaylistsEntityNotFoundException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new EntityNotFoundException());

        Response response = playlistEndpoint.deletePlaylist(1,"token");

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void deletePlayListTest() throws EntityNotFoundException, InternalServerException{
        when(playlistController.getAllPlaylists(any())).thenReturn(playlistResponse);

        Response response = playlistEndpoint.deletePlaylist(1,"token");

        Assert.assertEquals(response.toString(), playlistResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void addPlaylistsInternalServerException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new InternalServerException());

        Response response = playlistEndpoint.addPlaylist(playlistRequest, "token");

        Assert.assertEquals(500, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void addPlaylistsEntityNotFoundException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new EntityNotFoundException());

        Response response = playlistEndpoint.addPlaylist(playlistRequest, "token");

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void addPlayListTest() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenReturn(playlistResponse);

        Response response = playlistEndpoint.addPlaylist(playlistRequest, "token");

        Assert.assertEquals(response.toString(), playlistResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void updatePlaylistsInternalServerException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new InternalServerException());

        Response response = playlistEndpoint.updatePlaylist(playlistRequest, "token");

        Assert.assertEquals(500, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void updatePlaylistsEntityNotFoundException() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenThrow(new EntityNotFoundException());

        Response response = playlistEndpoint.updatePlaylist(playlistRequest, "token");

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void updatePlayListTest() throws EntityNotFoundException, InternalServerException {
        when(playlistController.getAllPlaylists(any())).thenReturn(playlistResponse);

        Response response = playlistEndpoint.updatePlaylist(playlistRequest, "token");

        Assert.assertEquals(response.toString(), playlistResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(playlistController, times(1)).getAllPlaylists("token");
    }

    @Test
    public void getTracksInPlaylistDatabaseException() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenThrow(new DatabaseConnectionException("", new Throwable()));

        Response response = playlistEndpoint.getTracksInPlaylist(1);

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }

    @Test
    public void getTracksInPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenReturn(trackResponse);

        Response response = playlistEndpoint.getTracksInPlaylist(1);

        Assert.assertEquals(response.toString(), trackResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }

    @Test
    public void deleteTrackFromPlaylistDatabaseException() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenThrow(new DatabaseConnectionException("", new Throwable()));

        Response response = playlistEndpoint.deleteTrackFromPlaylist(1, "1");

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }

    @Test
    public void deleteTrackFromPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenReturn(trackResponse);

        Response response = playlistEndpoint.deleteTrackFromPlaylist(1, "1");

        Assert.assertEquals(response.toString(), trackResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }

    @Test
    public void addTrackToPlaylistDatabaseException() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenThrow(new DatabaseConnectionException("", new Throwable()));

        Response response = playlistEndpoint.addTrackToPlaylist(1, new TrackRequest());

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }

    @Test
    public void addTrackToPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksInPlaylist(any())).thenReturn(trackResponse);

        Response response = playlistEndpoint.addTrackToPlaylist(1, new TrackRequest());

        Assert.assertEquals(response.toString(), trackResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(tracksController, times(1)).getTracksInPlaylist(1);
    }
}
