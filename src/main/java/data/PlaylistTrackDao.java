package data;

import data.interfaces.ConnectionManagerInterface;
import data.interfaces.PlaylistTrackDaoInterface;
import domain.PlaylistTrack;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlaylistTrackDao implements PlaylistTrackDaoInterface {

    private Logger LOGGER = Logger.getLogger(getClass().getName());
    private Connection connection;

    @Inject
    ConnectionManagerInterface connectionManager;

    @Override
    public List<PlaylistTrack> findAll() throws DatabaseQueryException, DatabaseConnectionException {
        List<PlaylistTrack> result = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlaylistTrack");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PlaylistTrack playlistTrack = buildPlaylistTrackFromResultSet(resultSet);
                result.add(playlistTrack);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.warning("Failed to get all playlistTracks from database");
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public List<PlaylistTrack> findByPlaylistId(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        List<PlaylistTrack> result = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlaylistTrack WHERE playlist_id = ?");
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                PlaylistTrack playlistTrack = buildPlaylistTrackFromResultSet(resultSet);
                result.add(playlistTrack);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to get playlistTracks by playlistId %s", playlistId));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public void removeTrackFromPlaylist(Integer playlistId, String trackId) throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `PlaylistTrack` WHERE `playlist_id` = ? AND `track_id` = ?");
            statement.setInt(1, playlistId);
            statement.setString(2, trackId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to remove PlaylistTrack with playlistId %s and trackId %s", playlistId, trackId));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public void addPlaylistTrack(PlaylistTrack playlistTrack) throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `PlaylistTrack` (`track_id`, `playlist_id`, `offlineAvailable`, `playCount`) VALUES (? ,?, ?, ?)");
            statement.setString(1, playlistTrack.getTrackId());
            statement.setInt(2, playlistTrack.getPlaylistId());
            statement.setBoolean(3, playlistTrack.isOfflineAvailable());
            statement.setInt(4, playlistTrack.getPlayCount());
            statement.execute();
        }
        catch (SQLException e) {
            LOGGER.warning(String.format("Failed to insert playlistTrack with trackId %s and playlistId %s", playlistTrack.getTrackId(), playlistTrack.getPlaylistId()));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    private PlaylistTrack buildPlaylistTrackFromResultSet(ResultSet resultSet) throws SQLException {
        String trackId = resultSet.getString("track_id");
        int playlistId = resultSet.getInt("playlist_id");
        boolean offlineAvailable = resultSet.getBoolean("offlineAvailable");
        int playCount = resultSet.getInt("playCount");

        return new PlaylistTrack(trackId, playlistId, offlineAvailable, playCount);
    }

    public void setConnectionManager(ConnectionManagerInterface connectionManager) {
        this.connectionManager = connectionManager;
    }
}
