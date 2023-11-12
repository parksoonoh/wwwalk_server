package Mayonnasie.wwwalk_server.walk;

import Mayonnasie.wwwalk_server.login.LoginForm;
import Mayonnasie.wwwalk_server.login.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;

    @GetMapping("/api/v1/route/getRouteInfo")
    public JsonForm getOneRouteInfo(@RequestParam(name = "route_id") String route_id) throws SQLException {
        log.info("route_id = " + route_id);
        return walkService.searchRouteInfo(route_id);
    }

    @GetMapping("/api/v1/route/getAllRouteInfo")
    public ArrayList<AllRouteForm> getAllroute() throws SQLException {
        return walkService.searchAllRouteInfo();
    }

    @GetMapping("/api/v1/pin/all")
    public ArrayList<PointForm> getAllPin() throws SQLException {
        return walkService.getAllPin();
    }

    @GetMapping("/api/v1/pin/info")
    public ArrayList<OnePinForm> getOnePin(@RequestParam(name = "pin_id")String pin_id) throws SQLException {
        return walkService.getOnePin(pin_id);
    }


    @PostMapping("/api/v1/route/start")
    public String walkStart(@RequestBody StartForm text) throws SQLException {
        StartForm startForm = new StartForm();
        startForm.setUser_id(text.getUser_id());
        startForm.setLat(text.getLat());
        startForm.setLon(text.getLon());
        log.info("text : " + text.getUser_id());
        log.info("startForm Lat : " + startForm.getLat());
        return walkService.walkstart(startForm);
    }

    @PostMapping("/api/v1/route/end")
    public String walkEnd(@RequestBody StartForm text) throws SQLException {
        log.info("walkend text : " + text);
        StartForm startForm = new StartForm();
        startForm.setUser_id(text.getUser_id());
        startForm.setLat(text.getLat());
        startForm.setLon(text.getLon());
        return walkService.walkend(startForm);
    }

    @PostMapping("/api/v1/route/addpoint")
    public String addpoint(@RequestBody StartForm text) throws SQLException {
        StartForm startForm = new StartForm();
        startForm.setUser_id(text.getUser_id());
        startForm.setLat(text.getLat());
        startForm.setLon(text.getLon());
        return walkService.addpoint(startForm);
    }

    @PostMapping("/api/v1/route/addpin")
    public String addpin(@RequestBody PinForm text) throws SQLException {
        PinForm pinForm = new PinForm();
        pinForm.setUser_id(text.getUser_id());
        pinForm.setLat(text.getLat());
        pinForm.setLon(text.getLon());
        pinForm.setPhoto_url(text.getPhoto_url());
        pinForm.setMemo(text.getMemo());
        pinForm.setAddress(text.getAddress());
        pinForm.setAddress_name(text.getAddress_name());
        return walkService.addpin(pinForm);
    }

    @PostMapping("/api/v1/route/modify")
    public String modifypin(@RequestBody ModifyForm text) throws SQLException {
        ModifyForm modifyForm = new ModifyForm();
        modifyForm.setUser_id(text.getUser_id());
        modifyForm.setRoute_id(text.getRoute_id());
        modifyForm.setRoute_name(text.getRoute_name());
        modifyForm.setSecurity(text.getSecurity());
        modifyForm.setPins_new(text.getPins_new());
        modifyForm.setPins_delete(text.getPins_delete());
        log.info("text tag : " + text.getTag());
        log.info("text del tag : " + text.getDel_tag());
        modifyForm.setTag(text.getTag());
        modifyForm.setDel_tag(text.getDel_tag());
        return walkService.modifypin(modifyForm);
    }
}
