package Mayonnasie.wwwalk_server.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/api/v1/auth/profile")
    public ProfileForm profile(@RequestParam("user_id") String user_id) throws SQLException {
        return loginService.profile(user_id);
    }

    @PostMapping("/api/v1/auth/login")
    public String login(@RequestBody LoginForm text) throws SQLException {
        LoginForm user = new LoginForm();
        user.setUser_id(text.getUser_id());
        user.setUser_name(text.getUser_name());
        user.setPhoto_url(text.getPhoto_url());
        return loginService.login(user);

    }
}
