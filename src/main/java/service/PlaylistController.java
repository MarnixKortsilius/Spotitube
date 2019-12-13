package service;

import data.interfaces.PlaylistDaoInterface;
import data.interfaces.UserDaoInterface;
import domain.Playlist;
import domain.User;
import dto.PlaylistForResponse;
import dto.PlaylistRequest;
import dto.PlaylistResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import service.interfaces.PlaylistControllerInterface;
import service.interfaces.TracksControllerInterface;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistController implements PlaylistControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    private PlaylistDaoInterface playlistDao;

    @Inject
    private TracksControllerInterface tracksController;

    @Inject
    private UserDaoInterface userDao;

    @Override
    public PlaylistResponse getAllPlaylists(String token) throws InternalServerException, EntityNotFoundException {
        List<PlaylistForResponse> playlistForResponses = new ArrayList<>();
        try {
            List<Playlist> playlistsDB = playlistDao.getPlaylists();
            Optional<User> user = userDao.getUserFromToken(token);

            playlistsDB.forEach(playlist -> {
                try {
                    playlist.setTracks(tracksController.getTracksInPlaylistAsList(playlist.getId()));
                    if(user.isPresent()){
                        if(playlist.getOwner().equals(user.get().getUser())){
                            PlaylistForResponse playlistForResponse = new PlaylistForResponse(playlist.getId(), playlist.getName(), true, playlist.getTracks());
                            playlistForResponses.add(playlistForResponse);
                        }
                        else{
                            PlaylistForResponse playlistForResponse = new PlaylistForResponse(playlist.getId(), playlist.getName(), false, playlist.getTracks());
                            playlistForResponses.add(playlistForResponse);
                        }
                    }
                    else{
                        PlaylistForResponse playlistForResponse = new PlaylistForResponse(playlist.getId(), playlist.getName(), false, playlist.getTracks());
                        playlistForResponses.add(playlistForResponse);
                    }
                } catch (DatabaseQueryException | DatabaseConnectionException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            });
            return new PlaylistResponse(playlistForResponses);
        } catch (DatabaseConnectionException | DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw (new InternalServerException());
        }
    }

    @Override
    public void deletePlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        playlistDao.deletePlaylist(playlistId);
    }

    @Override
    public void addPlaylist(PlaylistRequest playlist, String token) throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException {
        Optional<User> user = userDao.getUserFromToken(token);
        if (user.isPresent()) {
            playlistDao.addPlaylist(playlist.getName(), user.get().getUser());
        }
        else{
            playlistDao.addPlaylist(playlist.getName(), "");
        }
    }

    @Override
    public void updatePlaylist(PlaylistRequest playlist) throws DatabaseQueryException, DatabaseConnectionException {
        playlistDao.updateName(playlist.getId(), playlist.getName());
    }

    public void setPlaylistDao(PlaylistDaoInterface playlistDao){
        this.playlistDao = playlistDao;
    }

    public void setTracksController(TracksControllerInterface tracksController){
        this.tracksController = tracksController;
    }

    public void setUserDao(UserDaoInterface userDao){
        this.userDao = userDao;
    }
}
