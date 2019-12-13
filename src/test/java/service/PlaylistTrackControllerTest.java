package service;

import data.interfaces.PlaylistTrackDaoInterface;
import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaylistTrackControllerTest {

    private PlaylistTrackController playlistTrackController;
    private PlaylistTrack playlistTrack;

    @Mock
    PlaylistTrackDaoInterface playlistTrackDaoInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        playlistTrackController = new PlaylistTrackController();
        playlistTrackController.setPlaylistTrackDao(playlistTrackDaoInterface);

        playlistTrack = new PlaylistTrack("1", 1, false, 42);

    }

    @Test
    public void getPlaylistTracksTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(playlistTrackDaoInterface.findAll()).thenReturn(new ArrayList<>());

        playlistTrackController.getPlaylistTracks();

        verify(playlistTrackDaoInterface, times(1)).findAll();
    }

    @Test
    public void getPlaylistTracksByPlaylistIdTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(playlistTrackDaoInterface.findByPlaylistId(any())).thenReturn(new ArrayList<>());

        playlistTrackController.getPlaylistTracksByPlaylistId(1);

        verify(playlistTrackDaoInterface, times(1)).findByPlaylistId(1);
    }

    @Test
    public void removeTrackFromPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        doNothing().when(playlistTrackDaoInterface).removeTrackFromPlaylist(any(), any());

        playlistTrackController.removeTrackFromPlaylist(1, "1");

        verify(playlistTrackDaoInterface, times(1)).removeTrackFromPlaylist(1, "1");
    }

    @Test
    public void addTrackToPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        doNothing().when(playlistTrackDaoInterface).addPlaylistTrack(any());

        playlistTrackController.addTrackToPlaylist(playlistTrack);

        verify(playlistTrackDaoInterface, times(1)).addPlaylistTrack(playlistTrack);
    }
}
