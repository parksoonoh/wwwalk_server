package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.walk.PointForm;
import Mayonnasie.wwwalk_server.walk.StartForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Repository
public class PointOfRepository {
    public StartForm save(StartForm startForm) throws SQLException {
        String sql = "insert into POINT_OF(ROUTE_ID, POINT_ID, POINT_LAT, POINT_LON) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            String uuid = UUID.randomUUID().toString();
            pstmt.setString(1, findRouteId(startForm.getUser_id()));
            pstmt.setLong(2, System.currentTimeMillis());
            pstmt.setDouble(3, startForm.getLat());
            pstmt.setDouble(4, startForm.getLon());
            pstmt.executeUpdate();
            return startForm;
        } catch (SQLException e) {
            log.error("route db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public String findRouteId(String user_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where user_id = ? order by RDATE DESC";

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
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public Double findLON(String point_id) throws SQLException {
        String sql = "select * from POINT_OF where POINT_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, point_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getDouble("POINT_LON");
            }else{
                return 0D;
            }
        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public Double findLAT(String point_id) throws SQLException {
        String sql = "select * from POINT_OF where POINT_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, point_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getDouble("POINT_LAT");
            }else{
                return 0D;
            }
        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<PointForm> findallPoint(String route_id) throws SQLException {
        String sql = "select * from POINT_OF where ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, route_id);
            ArrayList<PointForm> allpoint = new ArrayList<>();
            rs = pstmt.executeQuery();
            while (rs.next()){
                PointForm pointForm = new PointForm();
                pointForm.setPoint_id(rs.getString("POINT_ID"));
                pointForm.setLon(rs.getDouble("POINT_LON"));
                pointForm.setLat(rs.getDouble("POINT_LAT"));
                allpoint.add(pointForm);
            }
            return allpoint;

        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
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
