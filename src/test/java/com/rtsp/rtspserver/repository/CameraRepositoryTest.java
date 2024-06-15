package com.rtsp.rtspserver.repository;
import com.rtsp.rtspserver.model.Camera;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CameraRepositoryTest {

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;

    @InjectMocks
    private CameraRepository cameraRepository;

//    @Test
//    void findById_ShouldReturnCamera_WhenCameraExists() throws SQLException {
//        when(conn.prepareStatement(anyString())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(true);
//        when(rs.getInt("camera_id")).thenReturn(1);
//        when(rs.getString("camera_name")).thenReturn("Test Camera");
//        when(rs.getString("camera_url")).thenReturn("rtsp://test.url:554");
//        when(rs.getInt("camera_type_id")).thenReturn(1);  // Використання нижнього регістру, як і в коді репозиторію
//
//        Camera camera = cameraRepository.findById(1);
//
//        assertNotNull(camera);
//        assertEquals(1, camera.getCameraId());
//        assertEquals("Test Camera", camera.getCameraName());
//        verify(stmt).executeQuery();
//        verify(rs).next();
//    }

    @Test
    void findById_ShouldReturnNull_WhenCameraDoesNotExist() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Camera camera = cameraRepository.findById(999);

        assertNull(camera);
        verify(stmt).executeQuery();
        verify(rs).next();
    }

//    @Test
//    void addCamera_ShouldReturnCamera_WhenCameraIsSaved() throws SQLException {
//        when(conn.prepareStatement(anyString(), anyInt())).thenReturn(stmt);
//        when(stmt.executeQuery()).thenReturn(rs);
//        when(rs.next()).thenReturn(true);
//        when(rs.getInt(1)).thenReturn(1);
//
//        Camera camera = new Camera("Test Camera", "rtsp://test.url:554", 1);
//        Camera savedCamera = cameraRepository.addCamera(camera);
//
//        assertNotNull(savedCamera);
//        assertEquals(1, savedCamera.getCameraId());
//        verify(stmt).executeUpdate();
//    }

    @Test
    void delete_ShouldReturnTrue_WhenCameraIsDeleted() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        boolean isDeleted = cameraRepository.delete(1);

        assertTrue(isDeleted);
        verify(stmt).executeUpdate();
    }
}
