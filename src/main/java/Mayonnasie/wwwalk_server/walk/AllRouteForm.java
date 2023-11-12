package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
@Data
public class AllRouteForm {
    private String userName;
    private String photo_url;
    private String route_id;
    private String route_name;
    private Long favorite_num;
    private Long comment_num;
    private Long distance;
    private Long duration;
    private ArrayList<PinSearchForm> pins;
    private ArrayList<String> tag;

    public AllRouteForm(){

    }

    public AllRouteForm(String userName, String photo_url, String route_id, String route_name, Long favorite_num, Long comment_num, Long distance, Long duration, ArrayList<PinSearchForm> pins, ArrayList<String> tag) {
        this.userName = userName;
        this.photo_url = photo_url;
        this.route_id = route_id;
        this.route_name = route_name;
        this.favorite_num = favorite_num;
        this.comment_num = comment_num;
        this.distance = distance;
        this.duration = duration;
        this.pins = pins;
        this.tag = tag;
    }

}
