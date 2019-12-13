package service;

import data.interfaces.PlaylistTrackDaoInterface;
import data.interfaces.TracksDaoInterface;
import domain.PlaylistTrack;
import domain.Track;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.helper.PlaylistTrackHelper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TracksControllerTest {

    private TracksController tracksController;
    private Track track;
    private List<Track> tracks;
    private TrackResponse trackResponse;

    @Mock
    TracksDaoInterface tracksDaoInterface;

    @Mock
    PlaylistTrackHelper playlistTrackHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tracksController = new TracksController();
        tracksController.setTracksDao(tracksDaoInterface);
        tracksController.setPlaylistTrackHelper(playlistTrackHelper);

        track = new Track("1", "title", "url", "performer", 10, "album", "date", "description", false);
        tracks = new ArrayList<>();
        tracks.add(track);
        trackResponse = new TrackResponse(tracks);
    }

    @Test
    public void getTracksTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksDaoInterface.findAll()).thenReturn(tracks);

        tracksController.getTracks();

        verify(tracksDaoInterface, times(1)).findAll();
    }

    @Test
    public void getTracksNotInPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksDaoInterface.findAll()).thenReturn(tracks);
        when(playlistTrackHelper.isTrackInPlaylist(any(), any())).thenReturn(true);

        List<Track> tracksInPlaylist = tracksController.getTracksNotInPlaylist(1);

        verify(tracksDaoInterface, times(1)).findAll();
        Assert.assertEquals(0, tracksInPlaylist.size());
    }

    @Test
    public void getTracksInPlaylistAsListTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksDaoInterface.findAll()).thenReturn(tracks);
        when(playlistTrackHelper.isTrackInPlaylist(any(), any())).thenReturn(true);

        List<Track> tracksInPlaylist = tracksController.getTracksInPlaylistAsList(1);

        verify(tracksDaoInterface, times(1)).findAll();
        Assert.assertEquals(tracks, tracksInPlaylist);
    }

    @Test
    public void addTrackToPlaylistTest() throws DatabaseQueryException, DatabaseConnectionException {
        when(tracksDaoInterface.findAll()).thenReturn(tracks);
        when(playlistTrackHelper.isTrackInPlaylist(any(), any())).thenReturn(true);

        TrackResponse tracksInPlaylist = tracksController.getTracksInPlaylist(1);

        verify(tracksDaoInterface, times(1)).findAll();
        Assert.assertEquals(trackResponse.getTracks(), tracksInPlaylist.getTracks());
    }
}
