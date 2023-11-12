package Mayonnasie.wwwalk_server.search;

import Mayonnasie.wwwalk_server.walk.PinSearchForm;

import java.util.ArrayList;

public class SelectRouteForm {
    private String route_id;
    private String route_name;
    private ArrayList<PinSearchForm> pins;
    private Long distance;
    private Long duration;
    private ArrayList<String> tag;
}
