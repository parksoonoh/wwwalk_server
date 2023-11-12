package Mayonnasie.wwwalk_server.walk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ModifyForm {
    @NotBlank
    private String user_id;
    @NotBlank
    private String route_id;
    @NotBlank
    private String route_name;
    @NotBlank
    private String security;


    private ArrayList<ModifyPinForm> pins_new;
    private ArrayList<ModifyPinForm> pins_delete;
    private ArrayList<String> tag;
    private ArrayList<String> del_tag;

    public ModifyForm(){

    }

    public ModifyForm(String user_id, String route_id, String route_name, String security, ArrayList<ModifyPinForm> pins_new, ArrayList<ModifyPinForm> pins_delete, ArrayList<String> tag, ArrayList<String> del_tag) {
        this.user_id = user_id;
        this.route_id = route_id;
        this.route_name = route_name;
        this.security = security;
        this.pins_new = pins_new;
        this.pins_delete = pins_delete;
        this.tag = tag;
        this.del_tag = del_tag;
    }
}
