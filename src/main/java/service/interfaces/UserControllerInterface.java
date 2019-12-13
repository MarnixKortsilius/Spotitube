package service.interfaces;

import dto.LoginRequest;
import dto.LoginResponse;
import exceptions.EntityNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidEntityException;
import exceptions.InvalidLoginException;

public interface UserControllerInterface {
    LoginResponse createLoginResponse(LoginRequest loginRequest)  throws EntityNotFoundException,
            InternalServerException, InvalidEntityException, InvalidLoginException;
}
