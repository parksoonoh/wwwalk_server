package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PinForm {

    @NotBlank
    private String user_id;
    @NotBlank
    private Double lon;
    @NotBlank
    private Double lat;
    @NotBlank
    private String photo_url;
    private String memo;
    private String address;
    private String address_name;

    public PinForm(){

    }

    public PinForm(String user_id, Double lon, Double lat, String photo_url, String memo, String address, String address_name) {
        this.user_id = user_id;
        this.lon = lon;
        this.lat = lat;
        this.photo_url = photo_url;
        this.memo = memo;
        this.address = address;
        this.address_name = address_name;
    }


}
