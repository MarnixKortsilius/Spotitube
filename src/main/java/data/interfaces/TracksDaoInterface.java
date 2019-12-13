package data.interfaces;

import domain.Track;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;

import java.util.List;

public interface TracksDaoInterface {
    List<Track> findAll() throws DatabaseQueryException, DatabaseConnectionException;
}
