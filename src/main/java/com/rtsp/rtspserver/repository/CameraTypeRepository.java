package com.rtsp.rtspserver.repository;

import com.rtsp.rtspserver.model.CameraType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CameraTypeRepository {
    private Connection conn;

    public CameraTypeRepository() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public CameraType getCameraTypeById(int typeId) {
        String sql = "SELECT * FROM cameratype WHERE type_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CameraType(rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("sdp_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CameraType> getAllCameraTypes() {
        List<CameraType> cameraTypes = new ArrayList<>();
        String sql = "SELECT * FROM cameratype";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CameraType cameraType = new CameraType(rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("sdp_text"));
                cameraTypes.add(cameraType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cameraTypes;
    }

    public void addCameraType(CameraType cameraType) {
        String sql = "INSERT INTO cameratype (type_name, sdp_text) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cameraType.getTypeName());
            pstmt.setString(2, cameraType.getSdpText());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateCameraType(CameraType cameraType) {
        String sql = "UPDATE cameratype SET type_name = ?, sdp_text = ? WHERE type_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cameraType.getTypeName());
            pstmt.setString(2, cameraType.getSdpText());
            pstmt.setInt(3, cameraType.getTypeId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCameraType(int typeId) {
        String sql = "DELETE FROM cameratype WHERE type_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
