package com.rtsp.rtspserver.service;
import com.rtsp.rtspserver.model.Role;
import com.rtsp.rtspserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role addRole(Role role) {
        return roleRepository.addRole(role);
    }

    public boolean deleteRole(int roleId) {
        return roleRepository.deleteRole(roleId);
    }

    public Role getRole(int roleId) {
        return roleRepository.findRoleById(roleId);
    }

    public List<Role> getAllRoles() {
        return roleRepository.getAllRoles();
    }

    public boolean updateRole(Role role) {
        return roleRepository.updateRole(role);
    }
}
