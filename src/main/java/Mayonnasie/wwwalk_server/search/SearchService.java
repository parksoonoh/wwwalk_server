package Mayonnasie.wwwalk_server.search;

import Mayonnasie.wwwalk_server.Repository.*;
import Mayonnasie.wwwalk_server.login.LoginForm;
import Mayonnasie.wwwalk_server.walk.AllRouteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PinInfoRepository pinInfoRepository;
    private final LikeOfRepository likeOfRepository;
    private final TagOfRepository tagOfRepository;
    private final RouteInfoRepository routeInfoRepository;

    public ArrayList<AllRouteForm> search(String keyword) throws SQLException {
        return pinInfoRepository.findRouteIdbykeyword(keyword);
    }

    public ArrayList<AllRouteForm> recommend(String user_id) throws SQLException {
        // like + favorite 한 id 값들
        ArrayList<String> collectRouteId = new ArrayList<>();
        for (String route_id : likeOfRepository.findByUserId(user_id)){
            if(!collectRouteId.contains(route_id)){
                collectRouteId.add(route_id);
            }
        }

        //collect된 route id를 바탕으로 Tag뽑기
        ArrayList<String> collectTags = new ArrayList<>();
        for (String route_id : collectRouteId){
            ArrayList<String> eachTags = tagOfRepository.findTagByRouteId(route_id);
            for (String Tag : eachTags){
                collectTags.add(Tag);
            }
        }

        // 뽑은 tag랑 맞는 route id 정보 뽑기
        log.info("collect Tags : " + collectTags);
        if (collectTags.size() == 0){
            return null;
        }
        // route id를 바탕으로 allRouteForm으로 정보 뽑기

        ArrayList<String> allRouteId = tagOfRepository.findRouteIdbyTag(collectTags);
        ArrayList<AllRouteForm> allRouteForms = new ArrayList<>();

        for(String route_id : allRouteId){
            allRouteForms.add(routeInfoRepository.findAllRouteInfoByRouteId(route_id));
        }

        return allRouteForms;
    }
}
