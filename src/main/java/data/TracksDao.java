package data;

import data.interfaces.ConnectionManagerInterface;
import data.interfaces.TracksDaoInterface;
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

public class TracksDao implements TracksDaoInterface {
    private Logger LOGGER = Logger.getLogger(getClass().getName());
    private Connection connection;

    @Inject
    ConnectionManagerInterface connectionManager;

    @Override
    public List<Track> findAll() throws DatabaseQueryException, DatabaseConnectionException {
        List<Track> result = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Tracks");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Track track = buildTrackFromResultSet(resultSet);
                result.add(track);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.warning("Failed to get all tracks from database");
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    private Track buildTrackFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String title = resultSet.getString("title");
        String url = resultSet.getString("url");
        String performer = resultSet.getString("performer");
        Integer duration = resultSet.getInt("duration");
        String album = resultSet.getString("album");
        String publicationDate = resultSet.getString("publication_date");
        String description = resultSet.getString("description");
        Boolean isSong = resultSet.getBoolean("isSong");

        return new Track(id, title, url, performer, duration, album, publicationDate, description, isSong);
    }

    public void setConnectionManager(ConnectionManagerInterface connectionManager) {
        this.connectionManager = connectionManager;
    }

}
