package service;

import data.interfaces.PlaylistTrackDaoInterface;
import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import service.interfaces.PlaylistTrackControllerInterface;

import javax.inject.Inject;
import java.util.List;

public class PlaylistTrackController implements PlaylistTrackControllerInterface {

    @Inject
    private PlaylistTrackDaoInterface playlistTrackDao;

    @Override
    public List<PlaylistTrack> getPlaylistTracks() throws DatabaseQueryException, DatabaseConnectionException {
        return playlistTrackDao.findAll();
    }

    @Override
    public List<PlaylistTrack> getPlaylistTracksByPlaylistId(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        return playlistTrackDao.findByPlaylistId(playlistId);
    }

    @Override
    public void removeTrackFromPlaylist(Integer playlistId, String trackId) throws DatabaseQueryException, DatabaseConnectionException {
        playlistTrackDao.removeTrackFromPlaylist(playlistId, trackId);
    }

    @Override
    public void addTrackToPlaylist(PlaylistTrack playlistTrack) throws DatabaseQueryException, DatabaseConnectionException {
        playlistTrackDao.addPlaylistTrack(playlistTrack);
    }

    public void setPlaylistTrackDao(PlaylistTrackDaoInterface playlistTrackDao){
        this.playlistTrackDao = playlistTrackDao;
    }
}
