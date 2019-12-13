package data.interfaces;

import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import java.util.List;

public interface PlaylistTrackDaoInterface {
    List<PlaylistTrack> findAll() throws DatabaseQueryException, DatabaseConnectionException;

    List<PlaylistTrack> findByPlaylistId(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    void removeTrackFromPlaylist(Integer playlistId, String trackId) throws DatabaseQueryException, DatabaseConnectionException;

    void addPlaylistTrack(PlaylistTrack playlistTrack) throws DatabaseQueryException, DatabaseConnectionException;
}
