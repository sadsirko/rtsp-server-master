package com.rtsp.rtspserver.repository;

import com.rtsp.rtspserver.model.Camera;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CameraRepository {
    private Connection conn;

    public CameraRepository() {
        try {
            this.conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Camera findById(int cameraId) {
        String sql = "SELECT * FROM Cameras WHERE camera_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Math.toIntExact(cameraId));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Camera(rs.getInt("camera_id"),
                        rs.getString("camera_name"),
                        rs.getString("camera_url"),
                        rs.getInt("camera_type_Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Camera addCamera(Camera camera) {
        String sql = "INSERT INTO Cameras (camera_name, camera_url, camera_type_id) VALUES (?, ?, ?) RETURNING camera_id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, camera.getCameraName());
            pstmt.setString(2, camera.getCameraUrl());
            pstmt.setInt(3, camera.getCameraTypeId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                camera.setCameraId(generatedId);
                return camera;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateCamera(Camera camera) {
        String sql = "UPDATE Cameras SET camera_name = ?, camera_url = ?, camera_type_id = ? WHERE camera_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, camera.getCameraName());
            pstmt.setString(2, camera.getCameraUrl());
            pstmt.setInt(3, camera.getCameraTypeId());
            pstmt.setInt(4, camera.getCameraId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean delete(int cameraId) {
        String sql = "DELETE FROM Cameras WHERE camera_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cameraId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Camera> getAllCameras() {
        List<Camera> cameras = new ArrayList<>();
        String sql = "SELECT * FROM Cameras";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Camera camera = new Camera(rs.getInt("camera_id"),
                        rs.getString("camera_name"),
                        rs.getString("camera_url"),
                        rs.getInt("camera_type_id"));
                cameras.add(camera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cameras;
    }


}
