package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class FavoriteOfRepository {

    public String save(String user_id, String route_id) throws SQLException {
        String sql = "insert into FAVORITE_OF(ROUTE_ID, USER_ID) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, route_id);
            pstmt.setString(2, user_id);
            pstmt.executeUpdate();
            return "1";
        } catch (SQLException e) {
            log.error("FAVORITEOF db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public String delete(String user_id, String route_id) throws SQLException {
        String sql = "delete from FAVORITE_OF where user_ID=? and route_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);
            pstmt.setString(2, route_id);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
            return "0";
        } catch (SQLException e) {
            log.info("db error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public String findbyid(String user_id, String route_id) throws SQLException {
        String sql = "select * from FAVORITE_OF where user_id = ? AND route_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);
            pstmt.setString(2, route_id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return "delete";
            }else{
                return "save";
            }
        }catch (SQLException e){
            log.error("FAVORTIE db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<String> findByUserId(String user_id) throws SQLException {
        String sql = "select * from FAVORITE_OF where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<String> favorite_route = new ArrayList<>();
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                favorite_route.add(rs.getString("ROUTE_ID"));
            }
            return favorite_route;
        }catch (SQLException e){
            log.error("FAVORTIE db findById error",e);
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
