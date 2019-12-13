package domain;

public class User {
    private String user;
    private String userName;
    private String password;
    private String token;

    public User(String user, String userName, String password, String token) {
        this.user = user;
        this.password = password;
        this.userName = userName;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
