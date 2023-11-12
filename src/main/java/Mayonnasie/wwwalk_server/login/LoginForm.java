package Mayonnasie.wwwalk_server.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    private String user_id;
    @NotBlank
    private String user_name;
    @NotBlank
    private String photo_url;


    public LoginForm(){
    }

    public LoginForm(String user_id, String user_name, String photo_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.photo_url = photo_url;
    }

}
