package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PointForm {
    @NotBlank
    private String point_id;
    @NotBlank
    private Double lon;
    @NotBlank
    private Double lat;

    public PointForm(){
    }

    public PointForm(String point_id, Double lon, Double lat) {
        this.point_id = point_id;
        this.lon = lon;
        this.lat = lat;
    }
}
