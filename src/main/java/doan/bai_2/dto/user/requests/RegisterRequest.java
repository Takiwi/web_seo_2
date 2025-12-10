package doan.bai_2.dto.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @Size(min = 4, max = 100, message = "username must be at least 4 characters.")
    private String username;

    @Email(message = "Email invalid!")
    private String email;

    @Size(min = 6, max = 30, message = "Password must be at least 6 characters.")
    private String password;

    public RegisterRequest(){}

    public RegisterRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
