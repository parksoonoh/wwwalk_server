package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.community.CommentForm;
import Mayonnasie.wwwalk_server.community.CommentReadForm;
import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.walk.PointForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Repository
public class CommentOfRepository {
    public String save(CommentForm commentForm) throws SQLException {
        String sql = "insert into COMMENT_OF(ROUTE_ID, USER_ID, COMMENT, COMMENT_ID, CDATE) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, commentForm.getRoute_id());
            pstmt.setString(2, commentForm.getUser_id());
            pstmt.setString(3, commentForm.getComment());
            pstmt.setString(4, UUID.randomUUID().toString());
            pstmt.setLong(5, System.currentTimeMillis());
            pstmt.executeUpdate();
            return "1";
        } catch (SQLException e) {
            log.error("COMMENT db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String comment_id) throws SQLException {
        String sql = "delete from COMMENT_OF where COMMENT_ID=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, comment_id);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public ArrayList<CommentReadForm> findAllComment(String route_id) throws SQLException {
        String sql = "select * from COMMENT_OF where ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, route_id);
            ArrayList<CommentReadForm> allComment = new ArrayList<>();
            rs = pstmt.executeQuery();
            while (rs.next()){
                CommentReadForm commentReadForm = new CommentReadForm();
                commentReadForm.setUserNickName(findNickName(rs.getString("USER_ID")));
                commentReadForm.setPhoto_url(findPhotoUrl(rs.getString("USER_ID")));
                commentReadForm.setComment(rs.getString("COMMENT"));
                commentReadForm.setComment_id(rs.getString("COMMENT_ID"));
                commentReadForm.setCommentDate(rs.getLong("CDATE"));
                allComment.add(commentReadForm);
            }
            return allComment;

        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    private String findNickName(String u_id) throws SQLException {
        String sql = "select * from USER_INFO where USER_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, u_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("USER_NAME");
            }else {
                return "유저 없음";
            }
        }catch (SQLException e){
            log.error("db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    private String findPhotoUrl(String u_id) throws SQLException {
        String sql = "select * from USER_INFO where USER_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, u_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("PHOTO_URL");
            }else {
                return "유저 없음";
            }
        }catch (SQLException e){
            log.error("db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){

        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error",e);
            }
        }

        if (stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error",e);
            }
        }
    }

    private Connection getConnection(){
        return DBConnectionUtil.getConnection();
    }

}
