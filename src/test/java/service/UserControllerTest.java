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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    @Mock
    private UserDaoInterface userDAO;
    private LoginRequest loginRequest;
    private LoginRequest invalidLoginRequest;
    private SQLException sqlException;
    private DatabaseQueryException databaseQueryException;
    private User user;
    private User userTokenNull;
    private User invalidPasswordUser;
    @Mock
    private List<String> tokens;
    private ArrayList<User> users;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        userController = new UserController();
        userController.setUserDao(userDAO);
        loginRequest = new LoginRequest();
        loginRequest.user = "user";
        loginRequest.password = "password";
        invalidLoginRequest = new LoginRequest();
        invalidLoginRequest.user = null;
        invalidLoginRequest.password = null;
        sqlException = new SQLException("SQLException");
        databaseQueryException = new DatabaseQueryException(sqlException.toString(), sqlException);
        user = new User("user", "username", "password", "token");
        userTokenNull = new User("user", "username", "password",  null);
        invalidPasswordUser = new User("user", "username", "invalidPassword", "token");
        users = new ArrayList<>();
        users.add(user);
    }

    @Test (expected = InvalidEntityException.class)
    public void createLoginResponseInvalidEntityException() throws EntityNotFoundException, InternalServerException,
            InvalidEntityException, InvalidLoginException {
        userController.createLoginResponse(invalidLoginRequest);
    }

    @Test (expected = InternalServerException.class)
    public void createLoginResponseInternalServerException() throws EntityNotFoundException, DatabaseQueryException,
            DatabaseConnectionException, InvalidEntityException, InternalServerException, InvalidLoginException {
        when(userDAO.getUser(anyString())).thenThrow(databaseQueryException);

        userController.createLoginResponse(loginRequest);

        verify(userDAO, times(1)).getUser("user");
    }

    @Test (expected = InvalidLoginException.class)
    public void createLoginResponseInvalidLoginException() throws EntityNotFoundException, InternalServerException,
            InvalidEntityException, DatabaseConnectionException, DatabaseQueryException, InvalidLoginException {
        when(userDAO.getUser(any())).thenReturn(Optional.of(user));

        loginRequest.password = invalidPasswordUser.getPassword();

        userController.createLoginResponse(loginRequest);

        verify(userDAO, times(1)).getUser("user");
    }

    @Test (expected = InternalServerException.class)
    public void createLoginResponseGenerateTokenForUserInternalServerException() throws InvalidEntityException,
            DatabaseQueryException, DatabaseConnectionException, DatabaseUpdateException, EntityNotFoundException,
            InvalidLoginException, InternalServerException {
        when(userDAO.getUser(anyString())).thenReturn(Optional.of(userTokenNull));
        when(userDAO.getTokens()).thenReturn(tokens);
        when(tokens.contains(any())).thenReturn(false);
        doThrow(new InvalidEntityException()).when(userDAO).updateUser(anyString(), any());

        userController.createLoginResponse(loginRequest);

        verify(userDAO, times(1)).getUser("user");
        verify(userDAO, times(1)).getTokens();
        verify(tokens, times(1)).contains(anyString());
        verify(userDAO, times(1)).updateUser("user", any(User.class));
    }

    @Test
    public void createLoginResponseSuccessfulTokenNull() throws InvalidEntityException, DatabaseQueryException,
            DatabaseConnectionException, DatabaseUpdateException, EntityNotFoundException, InvalidLoginException,
            InternalServerException {
        when(userDAO.getUser(anyString())).thenReturn(Optional.of(userTokenNull));
        when(userDAO.getTokens()).thenReturn(tokens);
        when(tokens.contains(anyObject())).thenReturn(true).thenReturn(false);
        doNothing().when(userDAO).updateUser(anyString(), any());

        LoginResponse response = userController.createLoginResponse(loginRequest);

        Assert.assertNotNull(response.getToken());
        Assert.assertEquals(14, response.getToken().length());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).updateUser(anyString(), userArgumentCaptor.capture());
        String responseToken = userArgumentCaptor.getValue().getToken();
        Assert.assertNotNull(responseToken);

        verify(userDAO, times(1)).getUser("user");
        verify(userDAO, times(1)).getTokens();
        verify(tokens, times(2)).contains(anyString());
        verify(userDAO, times(1)).updateUser(eq("user"), any(User.class));
    }

    @Test
    public void createLoginResponseTest() throws InvalidEntityException, DatabaseQueryException,
            DatabaseConnectionException, DatabaseUpdateException, EntityNotFoundException, InvalidLoginException,
            InternalServerException {
        when(userDAO.getUser(anyString())).thenReturn(Optional.of(user));
        when(userDAO.getTokens()).thenReturn(tokens);
        doNothing().when(userDAO).updateUser(anyString(), any());

        LoginResponse response = userController.createLoginResponse(loginRequest);

        Assert.assertEquals(user.getUserName(), response.getUser());
        Assert.assertEquals(user.getToken(), response.getToken());

        verify(userDAO, times(1)).getUser("user");
        verify(userDAO, times(0)).getTokens();
    }

}
