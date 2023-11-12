package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.walk.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Repository
public class PinInfoRepository {
    public PinForm save(PinForm pinForm, String pinId) throws SQLException {
        String sql = "insert into PIN_INFO(PIN_ID, MEMO, PHOTO_URL, ROUTE_ID, USER_ID, POINT_ID, ADDRESS, ADDRESS_NAME) values (?, ?, ?, ? ,? ,? ,? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            String calrouteid = findRouteId(pinForm.getUser_id());
            pstmt.setString(1, pinId);
            pstmt.setString(2, pinForm.getMemo());
            pstmt.setString(3, pinForm.getPhoto_url());
            pstmt.setString(4, calrouteid); //route id
            pstmt.setString(5, pinForm.getUser_id() ); //user id
            log.info("LAT : " + pinForm.getLat() + "LON : " + pinForm.getLon() + "route_id : " + calrouteid);
            pstmt.setString(6, findPointId(pinForm.getLat(), pinForm.getLon(), calrouteid)); //point id
            pstmt.setString(7, pinForm.getAddress()); //user id
            pstmt.setString(8, pinForm.getAddress_name()); //user id

            pstmt.executeUpdate();
            return pinForm;
        } catch (SQLException e) {
            log.error("pininforepository db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String delete_pin_id) throws SQLException {
        String sql = "delete from PIN_INFO where PIN_ID=?";

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

    public String findRouteId(String user_id) throws SQLException {
        String sql = "select * from ROUTE_INFO where user_id = ? ORDER BY RDATE DESC";

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

    public String findPointId(Double LAT, Double LON, String r_id) throws SQLException {
        String sql = "select * from POINT_OF where POINT_LON = ? AND POINT_LAT = ? AND ROUTE_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, LON);
            pstmt.setDouble(2, LAT);
            pstmt.setString(3, r_id);


            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("POINT_ID");
            }else{
                return "POINT id find error";
            }
        }catch (SQLException e){
            log.error("PIN INFO db findById error",e);
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

    public ArrayList<PointForm> getallPin() throws SQLException {
        String sql = "select * from PIN_INFO";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            ArrayList<PointForm> allPin = new ArrayList<>();
            rs = pstmt.executeQuery();
            while (rs.next()){
                PointForm pointForm = new PointForm();
                pointForm.setPoint_id(rs.getString("PIN_ID"));
                pointForm.setLon(Cal_Lon(rs.getString("POINT_ID")));
                pointForm.setLat(Cal_Lat(rs.getString("POINT_ID")));
                allPin.add(pointForm);
            }
            return allPin;

        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<OnePinForm> getOnePin(String pin_id) throws SQLException {
        String sql = "select * from PIN_INFO where PIN_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pin_id);
            ArrayList<OnePinForm> allPin = new ArrayList<>();
            rs = pstmt.executeQuery();
            if (rs.next()){
                OnePinForm onePinForm = new OnePinForm();
                onePinForm.setPhoto_url(rs.getString("PHOTO_URL"));
                onePinForm.setMemo(rs.getString("MEMO"));

                String route_id = rs.getString("ROUTE_ID");
                ArrayList<String> r_info = Cal_DDD(route_id);

                onePinForm.setRouteDistance(Double.valueOf(r_info.get(0)));
                onePinForm.setRouteDuration(Long.parseLong(r_info.get(1)));
                onePinForm.setRouteDate(Long.parseLong(r_info.get(2)));


                onePinForm.setPoints(findallPoint(route_id));

                String user_id = rs.getString("USER_ID");
                onePinForm.setUserNickName(findNickname(user_id));
                allPin.add(onePinForm);

            }
            return allPin;

        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public ArrayList<AllRouteForm> findRouteIdbykeyword(String keyword) throws SQLException {
        String sql = "select DISTINCT ROUTE_ID from PIN_INFO where ADDRESS_NAME LIKE CONCAT('%', ? ,'%')";
        ArrayList<AllRouteForm> selectRouteForms = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, keyword);
            rs = pstmt.executeQuery();
            while (rs.next()){
                selectRouteForms.add(findSelectRouteInfo(rs.getString("ROUTE_ID")));
            }
            return selectRouteForms;

        }catch (SQLException e){
            log.error("can't find route_id db findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public AllRouteForm findSelectRouteInfo(String route_id) throws SQLException {
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
                AllRouteForm allRouteForm = new AllRouteForm();
                allRouteForm.setRoute_id(route_id);
                allRouteForm.setRoute_name(rs.getString("ROUTE_NAME"));
                allRouteForm.setDistance(rs.getLong("LENGTH"));
                allRouteForm.setDuration(rs.getLong("DURATION"));
                allRouteForm.setPins(findselectPin(route_id));
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

    public ArrayList<PinSearchForm> findselectPin(String route_id) throws SQLException {
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

    private String findNickname(String u_id) throws SQLException {
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

    private Double Cal_Lon(String point_id) throws SQLException {
        String sql = "select * from POINT_OF where POINT_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,point_id);
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

    private Double Cal_Lat(String point_id) throws SQLException {
        String sql = "select * from POINT_OF where POINT_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,point_id);
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

    private ArrayList<String> Cal_DDD(String route_id) throws SQLException {
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
                ArrayList<String> ddd = new ArrayList<>();
                ddd.add(rs.getString("LENGTH"));
                ddd.add(rs.getString("DURATION"));
                ddd.add(rs.getString("RDATE"));
                return ddd;
            }else{
                return null;
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
