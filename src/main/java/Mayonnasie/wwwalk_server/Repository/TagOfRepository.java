package Mayonnasie.wwwalk_server.Repository;

import Mayonnasie.wwwalk_server.community.CommentForm;
import Mayonnasie.wwwalk_server.community.CommentReadForm;
import Mayonnasie.wwwalk_server.connection.DBConnectionUtil;
import Mayonnasie.wwwalk_server.walk.AllRouteForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class TagOfRepository {
    public void save(String route_id, ArrayList<String> tags) throws SQLException {
        String sql = "insert into Tag_OF(ROUTE_ID, TAG) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            log.info("insert tags : " + tags);
            for (String tag : tags){
                pstmt.setString(1, route_id);
                pstmt.setString(2, tag);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("TAGOF db insert error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String route_id, ArrayList<String> tags) throws SQLException {
        String sql = "delete from TAG_OF where ROUTE_ID = ? AND TAG = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            log.info("deltag : " + tags);
            for (String tag : tags){
                pstmt.setString(1, route_id);
                pstmt.setString(2, tag);
                pstmt.executeUpdate();
            }


        } catch (SQLException e) {
            log.info("db error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
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

    public ArrayList<String> findRouteIdbyTag(ArrayList<String> collectTag) throws SQLException {
        ArrayList<String> allRouteId = new ArrayList<>();
        String sql = "select distinct ROUTE_ID from TAG_OF where TAG in (" + "?, ".repeat(collectTag.size() - 1) + "?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            for(int i = 0; i < collectTag.size(); i++){
                pstmt.setString(i+1, collectTag.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()){
                allRouteId.add(rs.getString("ROUTE_ID"));
            }
            return allRouteId;
        }catch (SQLException e){
            log.error("findRouteId db findById error",e);
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
