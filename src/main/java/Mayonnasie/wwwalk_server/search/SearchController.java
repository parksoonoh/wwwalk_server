package Mayonnasie.wwwalk_server.search;

import Mayonnasie.wwwalk_server.login.LoginForm;
import Mayonnasie.wwwalk_server.login.LoginService;
import Mayonnasie.wwwalk_server.walk.AllRouteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/v1/route/search")
    public ArrayList<AllRouteForm> search(@RequestParam(name = "keyword") String keyword) throws SQLException {
        log.info("keyword : " + keyword);
        return searchService.search(keyword);
    }

    @GetMapping("/api/v1/route/recommend")
    public ArrayList<AllRouteForm> recommend(@RequestParam(name = "user_id") String user_id) throws SQLException {
        return searchService.recommend(user_id);
    }
}
