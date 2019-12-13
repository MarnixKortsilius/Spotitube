package data.interfaces;

import domain.User;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.DatabaseUpdateException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;

import java.util.List;
import java.util.Optional;

public interface UserDaoInterface {
    Optional<User> getUser(String user) throws DatabaseQueryException,
            EntityNotFoundException, DatabaseConnectionException;

    Optional<User> getUserFromToken(String token) throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException;

    void updateUser(String identifier, User user) throws InvalidEntityException, DatabaseUpdateException, DatabaseConnectionException;

    List<String> getTokens() throws DatabaseQueryException, DatabaseConnectionException;
}
