package data;

import data.interfaces.ConnectionManagerInterface;
import data.interfaces.UserDaoInterface;
import domain.User;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.DatabaseUpdateException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidEntityException;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao implements UserDaoInterface {
    private Logger LOGGER = Logger.getLogger(getClass().getName());
    private Connection connection;

    @Inject
    ConnectionManagerInterface connectionManager;

    @Override
    public Optional<User> getUser(String user) throws DatabaseQueryException,
            EntityNotFoundException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where user = ?");
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User returnUser = buildUserFromResultSet(resultSet);
                return Optional.of(returnUser);
            }
            throw (new EntityNotFoundException());
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to get user with name: %s from database", user));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    @Override
    public Optional<User> getUserFromToken(String token) throws EntityNotFoundException, DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where token = ?");
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User returnUser = buildUserFromResultSet(resultSet);
                return Optional.of(returnUser);
            }
            throw (new EntityNotFoundException());
        } catch (SQLException e) {
            LOGGER.warning(String.format("Failed to get user with token: %s from database", token));
            throw(new DatabaseQueryException(e.toString(), e));
        }
        finally {
            connectionManager.closeConnection(connection);
        }
    }

    private User buildUserFromResultSet(ResultSet resultSet) throws SQLException {
        String user = resultSet.getString("user");
        String password = resultSet.getString("password");
        String userName = resultSet.getString("userName");
        String token = resultSet.getString("token");

        return new User(user, userName, password, token);
    }

    @Override
    public void updateUser(String identifier, User user) throws InvalidEntityException, DatabaseUpdateException, DatabaseConnectionException {
        if (entityIsNotValid(user)) {
            throw(new InvalidEntityException());
        }
        try {
            connection = connectionManager.getConnection();

            String preparedQuery = "UPDATE users SET username = ?, password = ?, user = ?, token = ? WHERE user = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedQuery)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getUser());
                preparedStatement.setString(4, user.getToken());
                preparedStatement.setString(5, identifier);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw(new DatabaseUpdateException(e.toString(), e));
        }finally {
            connectionManager.closeConnection(connection);
        }
    }

    private boolean entityIsNotValid(User entity) {
        return entity.getUserName() == null || entity.getPassword() == null || entity.getUser() == null;
    }

    @Override
    public List<String> getTokens() throws DatabaseQueryException, DatabaseConnectionException {
        try {
            connection = connectionManager.getConnection();

            String preparedQuery = "SELECT token FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedQuery)) {
                ArrayList<String> tokens = new ArrayList<>();

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()) {
                        tokens.add(resultSet.getString(1));
                    }
                }

                return tokens;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw(new DatabaseQueryException(e.toString(), e));
        } finally {
            connectionManager.closeConnection(connection);
        }
    }

    public void setConnectionManager(ConnectionManagerInterface connectionManager) {
        this.connectionManager = connectionManager;
    }
}
