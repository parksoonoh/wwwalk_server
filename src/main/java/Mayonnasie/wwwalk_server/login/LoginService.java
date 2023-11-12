package Mayonnasie.wwwalk_server.login;

import Mayonnasie.wwwalk_server.Repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserInfoRepository userInfoRepository;
    public String login(LoginForm loginUser) throws SQLException {
        if (userInfoRepository.findById(loginUser) == "유저 있음") {
            log.info("유저 있음");
            return "유저 있음";
        }
        if (userInfoRepository.findById(loginUser) == "유저 없음") {
            userInfoRepository.save(loginUser);
            log.info("유저 없음");
            return "유저 새로 생성됨";
        }
        return null;
    }

    public ProfileForm profile(String user_id) throws SQLException {
        return userInfoRepository.getProfile(user_id);
    }
}
