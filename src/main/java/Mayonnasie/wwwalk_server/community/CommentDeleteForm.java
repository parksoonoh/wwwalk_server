package Mayonnasie.wwwalk_server.community;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDeleteForm {
    @NotBlank
    private String comment_id;

    public CommentDeleteForm(){

    }

    public CommentDeleteForm(String comment_id) {
        this.comment_id = comment_id;
    }
}
