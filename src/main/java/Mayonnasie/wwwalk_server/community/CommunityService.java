package Mayonnasie.wwwalk_server.community;

import Mayonnasie.wwwalk_server.Repository.CommentOfRepository;
import Mayonnasie.wwwalk_server.Repository.FavoriteOfRepository;
import Mayonnasie.wwwalk_server.Repository.LikeOfRepository;
import Mayonnasie.wwwalk_server.Repository.UserInfoRepository;
import Mayonnasie.wwwalk_server.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {
    private final LikeOfRepository likeOfRepository;
    private final FavoriteOfRepository favoriteOfRepository;
    private final CommentOfRepository commentOfRepository;

    public String like(LikeForm likeForm) throws SQLException {
        String IsUser = likeOfRepository.findbyid(likeForm.getUser_id(), likeForm.getRoute_id());
        if (IsUser.equals("save"))
            return likeOfRepository.save(likeForm.getUser_id(), likeForm.getRoute_id());

        if (IsUser.equals("delete"))
            return likeOfRepository.delete(likeForm.getUser_id(), likeForm.getRoute_id());
        return null;
    }

    public String favorite(LikeForm likeForm) throws SQLException {
        String IsUser = favoriteOfRepository.findbyid(likeForm.getUser_id(), likeForm.getRoute_id());
        if (IsUser.equals("save"))
            return favoriteOfRepository.save(likeForm.getUser_id(), likeForm.getRoute_id());

        if (IsUser.equals("delete"))
            return favoriteOfRepository.delete(likeForm.getUser_id(), likeForm.getRoute_id());
        return null;
    }

    public String commentsave(CommentForm commentForm) throws SQLException {
        commentOfRepository.save(commentForm);
        return "comment saved";
    }

    public String commentdelete(String comment_id) throws SQLException {
        commentOfRepository.delete(comment_id);
        return "comment deleted";
    }

    public ArrayList<CommentReadForm> commentRead(String route_id) throws SQLException {
        return commentOfRepository.findAllComment(route_id);
    }
}
