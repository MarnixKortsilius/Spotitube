package dto;

public class LoginResponse {
    private String user;
    private String token;

    public LoginResponse(String user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}