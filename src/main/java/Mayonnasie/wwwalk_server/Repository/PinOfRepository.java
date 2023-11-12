package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.walk.PinForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Slf4j
@Repository
public class PinOfRepository {
    public PinForm save(PinForm pinForm, String pinId) throws SQLException {
        String sql = "insert into PIN_OF(ROUTE_ID, PIN_ID) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, findRouteId(pinForm.getUser_id())); //route id
            pstmt.setString(2, pinId);
            pstmt.executeUpdate();
            return pinForm;
        } catch (SQLException e) {
            log.error("pinof db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public String findRouteId(String user_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where user_id = ? AND ROWNUM <= 1 order by RDATE DESC";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("ROUTE_ID");
            }else{
                return "route id find error";
            }
        }catch (SQLException e){
            log.error("POINT OF db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void delete(String delete_pin_id) throws SQLException {
        String sql = "delete from PIN_OF where PIN_ID=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            log.info("delete pin id : " + delete_pin_id);
            pstmt.setString(1, delete_pin_id);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("db error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
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
