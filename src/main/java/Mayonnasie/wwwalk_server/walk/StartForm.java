package Mayonnasie.wwwalk_server.walk;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartForm {

    @JsonProperty
    @NotBlank
    private String User_id;
    @NotBlank
    private Double Lon;
    @NotBlank
    private Double Lat;

    public StartForm(){

    }

    public StartForm(String user_id, Double lon, Double lat) {
        User_id = user_id;
        Lon = lon;
        Lat = lat;
    }
}
