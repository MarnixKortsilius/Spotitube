package service.interfaces;

import dto.PlaylistRequest;
import dto.PlaylistResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;

public interface PlaylistControllerInterface {
    PlaylistResponse getAllPlaylists(String token) throws InternalServerException, EntityNotFoundException;

    void deletePlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException;

    void addPlaylist(PlaylistRequest playlist, String token) throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException;

    void updatePlaylist(PlaylistRequest playlist) throws DatabaseQueryException, DatabaseConnectionException;
}
