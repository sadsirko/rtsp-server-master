package com.rtsp.rtspserver.repository;

import com.rtsp.rtspserver.model.Role;
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
class RoleRepositoryTest {

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    void findRoleById_ShouldReturnRole_WhenRoleExists() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("role_id")).thenReturn(1);
        when(rs.getString("role_name")).thenReturn("Admin");

        Role role = roleRepository.findRoleById(1);

        assertNotNull(role);
        assertEquals(1, role.getRoleId());
        assertEquals("Admin", role.getRoleName());
    }

    @Test
    void findRoleById_ShouldReturnNull_WhenRoleDoesNotExist() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Role role = roleRepository.findRoleById(999);

        assertNull(role);
    }
}
