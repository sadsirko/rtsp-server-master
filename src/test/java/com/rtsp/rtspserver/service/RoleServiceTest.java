package com.rtsp.rtspserver.service;

import com.rtsp.rtspserver.model.Role;
import com.rtsp.rtspserver.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void addRole_ShouldReturnRole_WhenRoleIsAdded() {
        Role role = new Role(1, "Admin");
        when(roleRepository.addRole(role)).thenReturn(role);

        Role addedRole = roleService.addRole(role);

        assertNotNull(addedRole);
        assertEquals("Admin", addedRole.getRoleName());
        verify(roleRepository).addRole(role);
    }

    @Test
    void deleteRole_ShouldReturnTrue_WhenRoleExists() {
        when(roleRepository.deleteRole(1)).thenReturn(true);

        assertTrue(roleService.deleteRole(1));
        verify(roleRepository).deleteRole(1);
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenRoleExists() {
        Role role = new Role(1, "Admin");
        when(roleRepository.findRoleById(1)).thenReturn(role);

        Role foundRole = roleService.getRole(1);

        assertNotNull(foundRole);
        assertEquals(role.getRoleId(), foundRole.getRoleId());
        verify(roleRepository).findRoleById(1);
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        List<Role> roles = List.of(new Role(1, "Admin"), new Role(2, "User"));
        when(roleRepository.getAllRoles()).thenReturn(roles);

        List<Role> foundRoles = roleService.getAllRoles();

        assertNotNull(foundRoles);
        assertFalse(foundRoles.isEmpty());
        assertEquals(2, foundRoles.size());
        verify(roleRepository).getAllRoles();
    }
}
