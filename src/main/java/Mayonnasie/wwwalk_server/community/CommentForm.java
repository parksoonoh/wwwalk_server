package Mayonnasie.wwwalk_server.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentForm {
    @NotBlank
    private String user_id;
    @NotBlank
    private String route_id;
    @NotEmpty
    private String comment;

    public CommentForm(){

    }

    public CommentForm(String user_id, String route_id, String comment) {
        this.user_id = user_id;
        this.route_id = route_id;
        this.comment = comment;
    }
}
