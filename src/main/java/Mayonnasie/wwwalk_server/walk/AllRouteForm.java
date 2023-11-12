package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
@Data
public class AllRouteForm {
    @NotBlank
    private String route_id;
    private String route_name;
    private Long distance;
    private Long duration;
    private ArrayList<PinSearchForm> pins;
    private ArrayList<String> tag;

    public AllRouteForm(){

    }

    public AllRouteForm(String route_id, String route_name, Long distance, Long duration, ArrayList<PinSearchForm> pins, ArrayList<String> tag) {
        this.route_id = route_id;
        this.route_name = route_name;
        this.distance = distance;
        this.duration = duration;
        this.pins = pins;
        this.tag = tag;
    }
}
