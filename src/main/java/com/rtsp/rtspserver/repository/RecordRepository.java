package com.rtsp.rtspserver.repository;

import com.rtsp.rtspserver.model.Recorder;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RecordRepository {
    private final Connection conn;

    public RecordRepository() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public Recorder findRecordById(int recordId) {
        String sql = "SELECT * FROM Records WHERE record_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Recorder(
                        rs.getInt("record_id"),
                        rs.getInt("camera_id"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("duration_time_min")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Recorder> getAllRecords() {
        List<Recorder> records = new ArrayList<>();
        String sql = "SELECT * FROM Records";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                records.add(new Recorder(
                        rs.getInt("record_id"),
                        rs.getInt("camera_id"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("duration_time_min")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public Recorder addRecord(Recorder record) {
        String sql = "INSERT INTO Records (camera_id, start_time, duration_time_min) VALUES (?, ?, ?) RETURNING record_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getCameraId());
            pstmt.setTimestamp(2, record.getStartTime());
            pstmt.setLong(3, record.getDurationTime());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                record.setRecordId(rs.getInt(1));
                return record;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateRecord(Recorder record) {
        String sql = "UPDATE Records SET end_time = ? WHERE record_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, record.getEndTime());
            pstmt.setInt(2, record.getRecordId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Recorder findRecordByCameraId(int cameraId) {
        String sql = "SELECT * FROM Records WHERE camera_id = ? AND end_time IS NULL ORDER BY start_time DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cameraId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Recorder(
                        rs.getInt("record_id"),
                        rs.getInt("camera_id"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("duration_time_min")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean deleteRecord(int recordId) {
        String sql = "DELETE FROM Records WHERE record_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recordId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
