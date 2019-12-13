package service;

import data.interfaces.TracksDaoInterface;
import domain.Track;
import dto.TrackResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import service.helper.PlaylistTrackHelper;
import service.interfaces.TracksControllerInterface;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class TracksController implements TracksControllerInterface {

    @Inject
    private TracksDaoInterface tracksDao;

    @Inject
    private PlaylistTrackHelper playlistTrackHelper;

    @Override
    public List<Track> getTracks() throws DatabaseQueryException, DatabaseConnectionException {
        return tracksDao.findAll();
    }

    @Override
    public List<Track> getTracksNotInPlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        List<Track> tracks = tracksDao.findAll();
        return tracks.stream().filter(track -> !playlistTrackHelper.isTrackInPlaylist(track, playlistId)).collect(Collectors.toList());
    }

    @Override
    public List<Track> getTracksInPlaylistAsList(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        List<Track> tracks = tracksDao.findAll();
        return tracks.stream().filter(track -> playlistTrackHelper.isTrackInPlaylist(track, playlistId)).collect(Collectors.toList());
    }

    @Override
    public TrackResponse getTracksInPlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        List<Track> tracks = tracksDao.findAll();
        List<Track> tracksInPlaylist =  tracks.stream().filter(track -> playlistTrackHelper.isTrackInPlaylist(track, playlistId)).collect(Collectors.toList());
        return new TrackResponse(tracksInPlaylist);
    }

    public void setTracksDao(TracksDaoInterface tracksDao){
        this.tracksDao = tracksDao;
    }

    public void setPlaylistTrackHelper(PlaylistTrackHelper playlistTrackHelper) {
        this.playlistTrackHelper = playlistTrackHelper;
    }
}
