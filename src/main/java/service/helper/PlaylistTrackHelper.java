package service.helper;

import domain.PlaylistTrack;
import domain.Track;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import service.interfaces.PlaylistTrackControllerInterface;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

public class PlaylistTrackHelper {
    private Logger LOGGER = Logger.getLogger(getClass().getName());

    @Inject
    private PlaylistTrackControllerInterface playlistTrackController;

    public Boolean isTrackInPlaylist(Track track, Integer playlistId) {
        List<PlaylistTrack> playlistTracks = null;
        try {
            playlistTracks = playlistTrackController.getPlaylistTracksByPlaylistId(playlistId);
        } catch (DatabaseQueryException | DatabaseConnectionException e) {
            LOGGER.warning("Failed to check if track is in playlist");
        }
        if(playlistTracks != null) {
            return playlistTracks.stream().anyMatch(playlistTrack -> playlistTrack.getTrackId().equals(track.getId()));
        }else{
            return false;
        }
    }

    public void setPlaylistTrackController(PlaylistTrackControllerInterface playlistTrackController){
        this.playlistTrackController = playlistTrackController;
    }
}
