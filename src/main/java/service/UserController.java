package service;

import data.interfaces.UserDaoInterface;
import domain.User;
import dto.LoginRequest;
import dto.LoginResponse;
import exceptions.DatabaseConnectionException;
import exceptions.DatabaseQueryException;
import exceptions.DatabaseUpdateException;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidEntityException;
import exceptions.InvalidLoginException;
import service.interfaces.UserControllerInterface;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements UserControllerInterface {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    private static final int TOKEN_LENGTH = 12;
    private static final String TOKEN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static final String TOKEN_SPLITTER = "-";
    private static final int TOKEN_SPLITTER_INTERVAL = 4;

    @Inject
    private UserDaoInterface userDao;

    @Override
    public LoginResponse createLoginResponse(LoginRequest loginRequest)  throws EntityNotFoundException,
            InternalServerException, InvalidEntityException, InvalidLoginException {
        if (isInvalidLoginRequest(loginRequest)) {
            throw(new InvalidEntityException());
        }

        try {
            Optional<User> userOptional = userDao.getUser(loginRequest.user);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(loginRequest.password)) {
                    String userName = user.getUserName();
                    String token = user.getToken();

                    if (token == null || token.equals("")) {

                        token = generateTokenForUser(user);
                    }

                    return new LoginResponse(userName, token);
                } else {
                    throw(new InvalidLoginException());
                }
            }
            else{
                throw (new EntityNotFoundException());
            }
        }catch (DatabaseConnectionException | DatabaseQueryException | DatabaseUpdateException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw(new InternalServerException());
        }
    }

    private String generateTokenForUser(User user) throws DatabaseQueryException, DatabaseConnectionException,
            InternalServerException, EntityNotFoundException, DatabaseUpdateException {
        List<String> tokens = userDao.getTokens();

        String token = generateToken();

        while (tokens.contains(token)) {
            token = generateToken();
        }

        user.setToken(token);
        try {
            userDao.updateUser(user.getUser(), user);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw(new InternalServerException());
        }

        return token;
    }

    private String generateToken() throws InternalServerException {
        if (TOKEN_SPLITTER_INTERVAL == 0) {
            throw(new InternalServerException());
        }

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        int i = 0;
        while (i < TOKEN_LENGTH && i >= 0) {
            if (stringBuilder.length() % (TOKEN_SPLITTER_INTERVAL + TOKEN_SPLITTER.length())
                    == TOKEN_SPLITTER_INTERVAL) {
                stringBuilder.append(TOKEN_SPLITTER);
            } else {
                char character = TOKEN_CHARACTERS.charAt(random.nextInt(TOKEN_CHARACTERS.length()));
                stringBuilder.append(character);
                i++;
            }
        }

        return stringBuilder.toString();
    }

    private boolean isInvalidLoginRequest(LoginRequest loginRequest) {
        return loginRequest.user == null || loginRequest.password == null;
    }

    public void setUserDao(UserDaoInterface userDao) {
        this.userDao = userDao;
    }
}