package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyPinForm {
    @NotBlank
    private String point_id;
    @NotBlank
    private String photo_url;

    private String memo;

    private String address;
    private String address_name;

    public ModifyPinForm(){

    }

    public ModifyPinForm(String point_id, String photo_url, String memo, String address, String address_name) {
        this.point_id = point_id;
        this.photo_url = photo_url;
        this.memo = memo;
        this.address = address;
        this.address_name = address_name;
    }
}
