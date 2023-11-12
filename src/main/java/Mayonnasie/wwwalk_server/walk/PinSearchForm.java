package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PinSearchForm {
    @NotBlank
    private String point_id;
    private String pin_id;
    private String photo_url;
    private String memo;
    private String address;
    private String address_Name;

    public PinSearchForm(){

    }

    public PinSearchForm(String point_id, String pin_id, String photo_url, String memo, String address, String address_Name) {
        this.point_id = point_id;
        this.pin_id = pin_id;
        this.photo_url = photo_url;
        this.memo = memo;
        this.address = address;
        this.address_Name = address_Name;
    }
}
