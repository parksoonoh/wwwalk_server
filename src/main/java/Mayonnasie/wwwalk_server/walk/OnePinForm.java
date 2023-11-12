package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class OnePinForm {
    private String userNickName;
    private Double routeDistance;
    private Long routeDuration;
    private Long routeDate;
    private String photo_url;
    private String memo;
    private ArrayList<PointForm> points;

    public OnePinForm(){

    }

    public OnePinForm(String userNickName, Double routeDistance, Long routeDuration, Long routeDate, String photo_url, String memo, ArrayList<PointForm> points) {
        this.userNickName = userNickName;
        this.routeDistance = routeDistance;
        this.routeDuration = routeDuration;
        this.routeDate = routeDate;
        this.photo_url = photo_url;
        this.memo = memo;
        this.points = points;
    }
}
