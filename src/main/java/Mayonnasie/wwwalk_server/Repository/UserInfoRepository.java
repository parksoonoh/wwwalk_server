package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.login.LoginForm;
import Mayonnasie.wwwalk_server.login.ProfileForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.NoSuchElementException;

import static java.lang.Math.round;

@Slf4j
@Repository
public class UserInfoRepository {
    public LoginForm save(LoginForm loginUser) throws SQLException {
        String sql = "insert into USER_INFO(user_id, user_name, photo_url,sum_dis, walk_count, work_state) values (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            log.info("user id : " + loginUser.getUser_id());
            pstmt.setString(1, loginUser.getUser_id());
            pstmt.setString(2, loginUser.getUser_name());
            pstmt.setString(3, loginUser.getPhoto_url());
            pstmt.setInt(4, 0);
            pstmt.setInt(5, 0);
            pstmt.setString(6, "STOP");
            pstmt.executeUpdate();
            return loginUser;
        } catch (SQLException e) {
            log.error("db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public String findById(LoginForm user) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUser_id());

            rs = pstmt.executeQuery();
            if (rs.next()){
                if ((!(user.getUser_name()).equals(rs.getString("USER_NAME"))) ||
                (!(user.getPhoto_url().equals(rs.getString("PHOTO_URL")))))
                        update(user);
                return "유저 있음";
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

    public void update(LoginForm user) throws SQLException {
        String sql = "update USER_INFO set user_Name=?, photo_URL = ? where user_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUser_name());
            pstmt.setString(2, user.getPhoto_url());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void startuser(String userId) throws SQLException {
        String sql = "update USER_INFO set work_state=? where user_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "WORK");
            pstmt.setString(2, userId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("STARTUSER update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void enduser(String userId) throws SQLException {
        String sql = "update USER_INFO set work_state=? where user_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "STOP");
            pstmt.setString(2, userId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("ENDUSER update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void updateUserSumDis(String userId, Long length) throws SQLException {
        String sql = "update USER_INFO set SUM_DIS = ?, WALK_COUNT = ? where user_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            Long total_dis = length + findSumDis(userId);
            pstmt.setLong(1, total_dis);
            pstmt.setLong(2, round(total_dis / 0.7));
            pstmt.setString(3, userId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public ProfileForm getProfile(String userId) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            if (rs.next()){
                ProfileForm profileForm = new ProfileForm();
                profileForm.setUserName(rs.getString("USER_NAME"));
                profileForm.setPhoto_url(rs.getString("PHOTO_URL"));
                profileForm.setSum_dis(rs.getLong("SUM_DIS"));
                profileForm.setWalk_count(rs.getLong("WALK_COUNT"));
                return profileForm;
            }else {
                return null;
            }
        }catch (SQLException e){
            log.error("db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    private Long findSumDis(String userId) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getLong("SUM_DIS");
            }else {
                return 0L;
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
