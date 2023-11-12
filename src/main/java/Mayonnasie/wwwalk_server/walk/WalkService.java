package Mayonnasie.wwwalk_server.walk;

import Mayonnasie.wwwalk_server.Repository.*;
import Mayonnasie.wwwalk_server.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.DeflaterOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkService {
    private final RouteInfoRepository routeInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final PointOfRepository pointOfRepository;
    private final PinInfoRepository pinInfoRepository;
    private final PinOfRepository pinOfRepository;
    private final TagOfRepository tagOfRepository;

    public String walkstart(StartForm startForm) throws SQLException {
        userInfoRepository.startuser(startForm.getUser_id());
        routeInfoRepository.save(startForm);
        pointOfRepository.save(startForm);
        return "insert new route";
    }

    public String walkend(StartForm startForm) throws SQLException {
        pointOfRepository.save(startForm);
        Map.Entry<String, Long> query = routeInfoRepository.updateend(startForm);
        userInfoRepository.enduser(startForm.getUser_id());
        userInfoRepository.updateUserSumDis(startForm.getUser_id(), query.getValue());
        return query.getKey();
    }

    public String addpoint(StartForm startForm) throws SQLException {
        pointOfRepository.save(startForm);
        return "point added";
    }

    public String addpin(PinForm pinForm) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        pinInfoRepository.save(pinForm, uuid);
        pinOfRepository.save(pinForm, uuid);
        return "pin added";
    }

    public String modifypin(ModifyForm modifyForm) throws SQLException {
        routeInfoRepository.update(modifyForm);
        for (ModifyPinForm new_pin : modifyForm.getPins_new())
            addpin(new PinForm(modifyForm.getUser_id(),
                    pointOfRepository.findLON(new_pin.getPoint_id()),
                    pointOfRepository.findLAT(new_pin.getPoint_id()),
                    new_pin.getPhoto_url(),
                    new_pin.getMemo(),
                    new_pin.getAddress(),
                    new_pin.getAddress_name()
                    ));
        for (ModifyPinForm delete_ModifyPinForm : modifyForm.getPins_delete()){
            pinOfRepository.delete(delete_ModifyPinForm.getPoint_id());
            pinInfoRepository.delete(delete_ModifyPinForm.getPoint_id());
        }
        log.info("service tag : " + modifyForm.getTag());
        log.info("service del tag : " + modifyForm.getDel_tag());
        tagOfRepository.save(modifyForm.getRoute_id(), modifyForm.getTag());
        tagOfRepository.delete(modifyForm.getRoute_id(), modifyForm.getDel_tag());
        return "route modified";
    }

    public JsonForm searchRouteInfo(String route_id) throws SQLException {
        JsonForm jsonForm = new JsonForm();
        log.info("route in in service : " + route_id);
        jsonForm.setRoute_id(route_id);
        String r_name = routeInfoRepository.findRouteName(route_id);
        jsonForm.setRoute_name(r_name);
        ArrayList<PointForm> PointArray = pointOfRepository.findallPoint(route_id);
        jsonForm.setPoints(PointArray);

        Map<String, ArrayList> temp2 = new HashMap<>();
        ArrayList<PinSearchForm> PinArray = pinInfoRepository.findallPin(route_id);
        jsonForm.setPins(PinArray);
        jsonForm.setTag(tagOfRepository.findTagByRouteId(route_id));
        return jsonForm;
    }

    public ArrayList<AllRouteForm> searchAllRouteInfo() throws SQLException {
        return routeInfoRepository.findAllRouteInfo();
    }

    public ArrayList<PointForm> getAllPin() throws SQLException {
        return pinInfoRepository.getallPin();
    }

    public ArrayList<OnePinForm> getOnePin(String pin_id) throws SQLException{
        return pinInfoRepository.getOnePin(pin_id);
    }
}
