package presentation;

import dto.LoginRequest;
import dto.LoginResponse;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidEntityException;
import exceptions.InvalidLoginException;
import service.interfaces.UserControllerInterface;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginEndpoint {

    @Inject
    UserControllerInterface userController;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPost(LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userController.createLoginResponse(loginRequest);
            return Response.status(201).entity(loginResponse).build();
        } catch (EntityNotFoundException | InvalidEntityException | InvalidLoginException e) {
            return Response.status(401).build();
        } catch (InternalServerException e) {
            return Response.status(500).build();
        }
    }

    public void setUserController(UserControllerInterface userController) {
        this.userController = userController;
    }
}
