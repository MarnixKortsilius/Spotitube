package data.interfaces;

import domain.Playlist;
import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import java.util.List;

public interface PlaylistDaoInterface {
    List<Playlist> getPlaylists() throws DatabaseQueryException, DatabaseConnectionException;

    void deletePlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    void addPlaylist(String playlistName, String user) throws DatabaseQueryException, DatabaseConnectionException;

    void updateName(Integer playlistId, String name) throws DatabaseQueryException, DatabaseConnectionException;
}
