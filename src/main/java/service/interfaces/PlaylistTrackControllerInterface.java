package service.interfaces;

import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import java.util.List;

public interface PlaylistTrackControllerInterface {
    List<PlaylistTrack> getPlaylistTracks() throws DatabaseQueryException, DatabaseConnectionException;

    List<PlaylistTrack> getPlaylistTracksByPlaylistId(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    void removeTrackFromPlaylist(Integer playlistId, String trackId) throws DatabaseQueryException, DatabaseConnectionException;

    void addTrackToPlaylist(PlaylistTrack playlistTrack) throws DatabaseQueryException, DatabaseConnectionException;
}
