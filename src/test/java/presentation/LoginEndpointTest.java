package presentation;

import dto.LoginRequest;
import dto.LoginResponse;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidEntityException;
import exceptions.InvalidLoginException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.interfaces.UserControllerInterface;

import javax.ws.rs.core.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginEndpointTest {

    LoginEndpoint loginEndpoint;
    @Mock
    UserControllerInterface userController;
    LoginRequest loginRequest;
    LoginResponse loginResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        loginEndpoint = new LoginEndpoint();
        loginEndpoint.setUserController(userController);

        loginRequest = new LoginRequest();
        loginRequest.user = "user";
        loginRequest.password = "password";
        loginResponse = new LoginResponse("user","token");
    }

    @Test
    public void loginPostInternalServerException() throws EntityNotFoundException, InternalServerException,
            InvalidEntityException, InvalidLoginException {
        when(userController.createLoginResponse(any())).thenThrow(new InternalServerException());

        Response response = loginEndpoint.loginPost(loginRequest);

        Assert.assertEquals(500, response.getStatus());
        verify(userController, times(1)).createLoginResponse(loginRequest);
    }

    @Test
    public void loginPostInvalidEntityException() throws EntityNotFoundException, InternalServerException,
            InvalidEntityException, InvalidLoginException {
        when(userController.createLoginResponse(any())).thenThrow(new InvalidEntityException());

        Response response = loginEndpoint.loginPost(loginRequest);

        Assert.assertEquals(response.toString(), 401, response.getStatus());
        verify(userController, times(1)).createLoginResponse(loginRequest);
    }

    @Test
    public void loginPostSuccessful() throws EntityNotFoundException, InternalServerException, InvalidEntityException,
            InvalidLoginException {
        when(userController.createLoginResponse(any())).thenReturn(loginResponse);

        Response response = loginEndpoint.loginPost(loginRequest);

        Assert.assertEquals(response.toString(), loginResponse, response.getEntity());
        Assert.assertEquals(response.toString(), 201, response.getStatus());
        verify(userController, times(1)).createLoginResponse(loginRequest);
    }
}
