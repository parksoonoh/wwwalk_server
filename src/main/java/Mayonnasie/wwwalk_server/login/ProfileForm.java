package Mayonnasie.wwwalk_server.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileForm {
    @NotBlank
    private String userName;
    @NotBlank
    private String photo_url;
    private Long sum_dis;
    private Long walk_count;

    public ProfileForm(){

    }

    public ProfileForm(String userName, String photo_url, Long sum_dis, Long walk_count) {
        this.userName = userName;
        this.photo_url = photo_url;
        this.sum_dis = sum_dis;
        this.walk_count = walk_count;
    }
}
