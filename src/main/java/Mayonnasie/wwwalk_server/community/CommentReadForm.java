package Mayonnasie.wwwalk_server.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentReadForm {
    @NotBlank
    private String userNickName;

    private String photo_url;

    @NotBlank
    private String comment;

    private String comment_id;

    @NotEmpty
    private Long commentDate;
}
