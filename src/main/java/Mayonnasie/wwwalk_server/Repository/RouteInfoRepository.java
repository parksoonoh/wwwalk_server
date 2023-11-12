package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.login.LoginForm;
import Mayonnasie.wwwalk_server.walk.AllRouteForm;
import Mayonnasie.wwwalk_server.walk.ModifyForm;
import Mayonnasie.wwwalk_server.walk.PinSearchForm;
import Mayonnasie.wwwalk_server.walk.StartForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import static java.lang.Math.*;

@Slf4j
@Repository
public class RouteInfoRepository {
    public StartForm save(StartForm startForm) throws SQLException {
        String sql = "insert into ROUTE_INFO(ROUTE_ID, ROUTE_NAME, START_POINT_LAT, START_POINT_LON, " +
                "END_POINT_LAT, END_POINT_LON, USER_ID, DURATION, LENGTH, MUSIC_URL, RDATE, PUBLIC) values (?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            String uuid = UUID.randomUUID().toString();
            log.info("Route id : " + uuid);
            pstmt.setString(1, uuid);
            pstmt.setString(2, null);
            pstmt.setDouble(3, startForm.getLat());
            pstmt.setDouble(4, startForm.getLon());
            pstmt.setString(5, null);
            pstmt.setString(6, null);
            pstmt.setString(7, startForm.getUser_id());
            pstmt.setString(8, null);
            pstmt.setString(9, null);
            pstmt.setString(10, null);
            log.info("time : " + System.currentTimeMillis());
            pstmt.setLong(11, System.currentTimeMillis());
            pstmt.setString(12, "PRIVATE");

            pstmt.executeUpdate();
            return startForm;
        } catch (SQLException e) {
            log.error("route db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public Map.Entry<String, Long> updateend(StartForm startForm) throws SQLException {
        String sql = "UPDATE ROUTE_INFO SET END_POINT_LAT = ?, END_POINT_LON = ?, DURATION = ?, LENGTH = ?, RDATE = ?, ROUTE_NAME = ? WHERE ROUTE_ID = ? ";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            String f_routeid = findRouteId(startForm.getUser_id());
            log.info("route_id : " + f_routeid);
            pstmt.setDouble(1, startForm.getLat());
            pstmt.setDouble(2, startForm.getLon());
            pstmt.setLong(3, cal_Duration(f_routeid));
            log.info("cal_length(f_routeid) : "+ cal_length(f_routeid));
            Long length = round(cal_length(f_routeid) * 1000);
            log.info("length : " + length);
            pstmt.setLong(4, length);
            pstmt.setLong(5, System.currentTimeMillis());
            pstmt.setString(6, "무제");
            pstmt.setString(7, f_routeid);
            pstmt.executeUpdate();
            Map.Entry<String, Long> result = new AbstractMap.SimpleEntry<>(f_routeid, length);
            return result;
        } catch (SQLException e) {
            log.error("route db update end insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public ModifyForm update(ModifyForm modifyForm) throws SQLException {
        String sql = "UPDATE ROUTE_INFO SET ROUTE_NAME = ?, PUBLIC = ? WHERE ROUTE_ID = ? ";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, modifyForm.getRoute_name());
            pstmt.setString(2, modifyForm.getSecurity());
            pstmt.setString(3, modifyForm.getRoute_id());
            pstmt.executeUpdate();
            return modifyForm;
        } catch (SQLException e) {
            log.error("route modify db insert error", e);
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
            log.error("findRouteId db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public Double cal_length(String r_id) throws SQLException {
        String sql = "select * from POINT_OF where ROUTE_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, r_id);
            rs = pstmt.executeQuery();
            int idx = 0;
            Double before_lon = 1D;
            Double before_lat = 1D;
            Double distance = 0D;
            while (rs.next()){
                if (idx == 0){
                    before_lon = rs.getDouble("POINT_LON");
                    before_lat = rs.getDouble("POINT_LAT");
                }
                else{
                    Double after_lon = rs.getDouble("POINT_LON");
                    Double after_lat = rs.getDouble("POINT_LAT");
                    log.info("before : " + before_lon + ", " + before_lat);
                    log.info("after : " + after_lon + ", " + after_lat);
                    distance += Math.acos(Math.sin(Math.toRadians(before_lat))*Math.sin(Math.toRadians(after_lat)) +
                            Math.cos(Math.toRadians(before_lat))*Math.cos(Math.toRadians(after_lat))*Math.cos(Math.toRadians(before_lon - after_lon))) * 6371;

                    before_lon = after_lon;
                    before_lat = after_lat;
                }
                idx++;
            }
            return distance;
        }catch (SQLException e){
            log.error("routeinfo db cal length error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public Long cal_Duration(String r_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where ROUTE_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, r_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return (System.currentTimeMillis() - rs.getLong("RDATE"));
            }else{
                return 0L;
            }
        }catch (SQLException e){
            log.error("RouteInfoRepository cal duration error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public String findRouteName(String route_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, route_id);

            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("ROUTE_NAME");
            }else{
                return "findRouteName route id find error";
            }
        }catch (SQLException e){
            log.error("findRouteId db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<AllRouteForm> findAllRouteInfo() throws SQLException {
        ArrayList<AllRouteForm> allRouteForms = new ArrayList<>();
        String sql = "select * from ROUTE_INFO where PUBLIC = 'PUBLIC'";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next()){
                AllRouteForm allRouteForm = new AllRouteForm();
                allRouteForm.setRoute_id(rs.getString("ROUTE_ID"));
                allRouteForm.setRoute_name(rs.getString("ROUTE_NAME"));
                allRouteForm.setDistance(rs.getLong("LENGTH"));
                allRouteForm.setDuration(rs.getLong("DURATION"));
                allRouteForm.setPins(findallPin(rs.getString("ROUTE_ID")));
                allRouteForm.setTag(findTagByRouteId(rs.getString("ROUTE_ID")));
                allRouteForms.add(allRouteForm);
            }
            return allRouteForms;
        }catch (SQLException e){
            log.error("findRouteId db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public AllRouteForm findAllRouteInfoByRouteId(String route_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where PUBLIC = 'PUBLIC' and ROUTE_ID = route_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            if(rs.next()){
                AllRouteForm allRouteForm = new AllRouteForm();
                allRouteForm.setRoute_id(rs.getString("ROUTE_ID"));
                allRouteForm.setRoute_name(rs.getString("ROUTE_NAME"));
                allRouteForm.setDistance(rs.getLong("LENGTH"));
                allRouteForm.setDuration(rs.getLong("DURATION"));
                allRouteForm.setPins(findallPin(rs.getString("ROUTE_ID")));
                allRouteForm.setTag(findTagByRouteId(rs.getString("ROUTE_ID")));
                return allRouteForm;
            }else{
                return null;
            }
        }catch (SQLException e){
            log.error("findRouteId db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<String> findTagByRouteId(String route_id) throws SQLException {
        ArrayList<String> allTag = new ArrayList<>();
        String sql = "select * from TAG_OF where ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, route_id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                allTag.add(rs.getString("Tag"));
            }
            return allTag;
        }catch (SQLException e){
            log.error("findRouteId db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<PinSearchForm> findallPin(String route_id) throws SQLException {
        String sql = "select * from PIN_INFO where ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, route_id);
            ArrayList<PinSearchForm> allPin = new ArrayList<>();
            rs = pstmt.executeQuery();
            while (rs.next()){
                PinSearchForm pinSearchForm = new PinSearchForm();
                pinSearchForm.setPoint_id(rs.getString("POINT_ID"));
                pinSearchForm.setPin_id(rs.getString("PIN_ID"));
                pinSearchForm.setPhoto_url(rs.getString("PHOTO_URL"));
                pinSearchForm.setMemo(rs.getString("MEMO"));
                pinSearchForm.setAddress(rs.getString("ADDRESS"));
                pinSearchForm.setAddress_Name(rs.getString("ADDRESS_NAME"));
                log.info("PIN : " + pinSearchForm.getPin_id());
                allPin.add(pinSearchForm);
            }
            return allPin;

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
