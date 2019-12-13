package service.interfaces;

import domain.Track;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import java.util.List;

public interface TracksControllerInterface {
    List<Track> getTracks() throws DatabaseQueryException, DatabaseConnectionException;

    List<Track> getTracksNotInPlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    List<Track> getTracksInPlaylistAsList(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    TrackResponse getTracksInPlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;
}
