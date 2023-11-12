package Mayonnasie.wwwalk_server.community;

import Mayonnasie.wwwalk_server.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping("/api/v1/route/comment")
    public ArrayList<CommentReadForm> Comment(@RequestParam(name = "route_id")String route_id) throws SQLException {
        return communityService.commentRead(route_id);
    }

    @PostMapping("/api/v1/route/like")
    public String like(@RequestBody LikeForm text) throws SQLException {
        LikeForm likeForm = new LikeForm();
        likeForm.setUser_id(text.getUser_id());
        likeForm.setRoute_id(text.getRoute_id());
        return communityService.like(likeForm);

    }

    @PostMapping("/api/v1/route/favorite")
    public String Favorite(@RequestBody LikeForm text) throws SQLException {
        LikeForm likeForm = new LikeForm();
        likeForm.setUser_id(text.getUser_id());
        likeForm.setRoute_id(text.getRoute_id());
        return communityService.favorite(likeForm);
    }

    @PostMapping("/api/v1/route/comment")
    public String Comment(@RequestBody CommentForm text) throws SQLException {
        CommentForm commentForm = new CommentForm();
        commentForm.setUser_id(text.getUser_id());
        commentForm.setRoute_id(text.getRoute_id());
        commentForm.setComment(text.getComment());
        return communityService.commentsave(commentForm);
    }

    @PostMapping("/api/v1/route/comment/delete")
    public String Delete_Comment(@RequestBody CommentDeleteForm text) throws SQLException {
        return communityService.commentdelete(text.getComment_id());
    }
}
