package Mayonnasie.wwwalk_server.community;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LikeForm {
    @NotBlank
    private String user_id;
    @NotBlank
    private String route_id;

    public LikeForm(){

    }

    public LikeForm(String user_id, String route_id) {
        this.user_id = user_id;
        this.route_id = route_id;
    }
}
