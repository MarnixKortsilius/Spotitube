package presentation;

import dto.LoginRequest;
import dto.LoginResponse;
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
import service.interfaces.TracksControllerInterface;
import service.interfaces.UserControllerInterface;

import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TracksEndpointTest {

    TracksEndpoint tracksEndpoint;
    @Mock
    TracksControllerInterface tracksController;
    LoginRequest loginRequest;
    LoginResponse loginResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tracksEndpoint = new TracksEndpoint();
        tracksEndpoint.setTracksController(tracksController);
    }

    @Test
    public void getTracksDatabaseException() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracks()).thenThrow(new DatabaseQueryException("", new Throwable()));

        Response response = tracksEndpoint.getTracks(0);

        Assert.assertEquals(response.toString(), 400, response.getStatus());
        verify(tracksController, times(1)).getTracks();
    }

    @Test
    public void getTracksTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracks()).thenReturn(new ArrayList<>());

        Response response = tracksEndpoint.getTracks(0);

        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(tracksController, times(1)).getTracks();
    }

    @Test
    public void getTracksNotInPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksController.getTracksNotInPlaylist(1)).thenReturn(new ArrayList<>());

        Response response = tracksEndpoint.getTracks(1);

        Assert.assertEquals(response.toString(), 200, response.getStatus());
        verify(tracksController, times(1)).getTracksNotInPlaylist(1);
    }
}
