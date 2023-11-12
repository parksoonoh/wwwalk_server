package Mayonnasie.wwwalk_server.walk;

import lombok.Data;

@Data
public class EndForm {
    private String route_id;

    public EndForm(){

    }
    public EndForm(String route_id) {
        this.route_id = route_id;
    }
}
