package com.rtsp.rtspserver.repository;

import java.sql.Connection;
import java.sql.SQLException;
import com.rtsp.rtspserver.model.ConnectionLog;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ConnectionLogRepository {
    private Connection conn;

    public ConnectionLogRepository() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }


    public ConnectionLog getConnectionLogById(int connectionId) {
        String sql = "SELECT * FROM ConnectionLogs WHERE connection_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, connectionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ConnectionLog(rs.getInt("connection_id"),
                        rs.getInt("camera_id"),
                        rs.getTimestamp("connection_time"),
                        rs.getTimestamp("disconnection_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ConnectionLog> getAllConnectionLogs() {
        List<ConnectionLog> connectionLogs = new ArrayList<>();
        String sql = "SELECT * FROM ConnectionLogs";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ConnectionLog connectionLog = new ConnectionLog(rs.getInt("connection_id"),
                        rs.getInt("camera_id"),
                        rs.getTimestamp("connection_time"),
                        rs.getTimestamp("disconnection_time"));
                connectionLogs.add(connectionLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionLogs;
    }

    public void addConnectionLog(ConnectionLog connectionLog) {
        String sql = "INSERT INTO ConnectionLogs (camera_id, connection_time, disconnection_time) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, connectionLog.getCameraId());
            pstmt.setTimestamp(2, connectionLog.getConnectionTime());
            pstmt.setTimestamp(3, connectionLog.getDisconnectionTime());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateConnectionLog(ConnectionLog connectionLog) {
        String sql = "UPDATE ConnectionLogs SET camera_id = ?, connection_time = ?, disconnection_time = ? WHERE connection_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, connectionLog.getCameraId());
            pstmt.setTimestamp(2, connectionLog.getConnectionTime());
            pstmt.setTimestamp(3, connectionLog.getDisconnectionTime());
            pstmt.setInt(4, connectionLog.getConnectionId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteConnectionLog(int connectionId) {
        String sql = "DELETE FROM ConnectionLogs WHERE connection_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, connectionId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
