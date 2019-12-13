package data;

import data.interfaces.ConnectionManagerInterface;
import data.interfaces.PlaylistDaoInterface;
import domain.Playlist;
import domain.Track;
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

public class PlaylistDao implements PlaylistDaoInterface {
    private Logger LOGGER = Logger.getLogger(getClass().getName());
    private Connection connection;

    @Inject
    ConnectionManagerInterface connectionManager;

    @Override
    public List<Playlist> getPlaylists() throws DatabaseQueryException, DatabaseConnectionException {
        List<Playlist> result = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Playlists`");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Playlist playlist = buildPlaylistsFromResult(resultSet);
                result.add(playlist);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.warning("Failed to get all playlists from database");
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public void deletePlaylist(Integer playlistId) throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `Playlists` WHERE `id` = ?");
            statement.setInt(1, playlistId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to delete playlist with id %s", playlistId));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public void addPlaylist(String playlistName, String user) throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `Playlists` (`name`, `owner`) VALUE (?, ?)");
            statement.setString(1, playlistName);
            statement.setString(2, user);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to insert playlist with values %s, %s into database", playlistName, user));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public void updateName(Integer playlistId, String name) throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `Playlists` SET `name` = ? WHERE `id` = ?");
            statement.setString(1, name);
            statement.setInt(2, playlistId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to update name of playlist with id %n and name %s", playlistId, name));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    private Playlist buildPlaylistsFromResult(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String owner = resultSet.getString("owner");
        List<Track> list = new ArrayList<>();
        return new Playlist(id, name, owner, list);
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
