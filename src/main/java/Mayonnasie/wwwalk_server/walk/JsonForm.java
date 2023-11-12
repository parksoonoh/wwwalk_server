package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class JsonForm {
    @NotBlank
    private String route_id;
    private String route_name;
    @NotBlank
    private ArrayList<PointForm> points;
    private ArrayList<PinSearchForm> pins;
    private ArrayList<String> tag;
    public JsonForm(){

    }

    public JsonForm(String route_id, String route_name, ArrayList<PointForm> points, ArrayList<PinSearchForm> pins, ArrayList<String> tag) {
        this.route_id = route_id;
        this.route_name = route_name;
        this.points = points;
        this.pins = pins;
        this.tag = tag;
    }

}
