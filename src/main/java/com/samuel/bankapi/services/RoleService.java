package com.samuel.bankapi.services;

import com.samuel.bankapi.models.entities.Role;
import com.samuel.bankapi.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RoleService {
    @Autowired
    private RoleRepo roleRepo;

    public List<Role> getRoles() {
        return StreamSupport.stream(roleRepo.findAll().spliterator(), false).toList();
    }

    public Role createRole(Role role) {
        return roleRepo.save(role);
    }

    public Role updateRole(String id, Role role) {
        role.setId(id);
        return roleRepo.findById(id).map(
                existingRole -> {
                    Optional.ofNullable(role.getRoleName()).ifPresent(existingRole::setRoleName);
                    Optional.ofNullable(role.getDescription()).ifPresent(existingRole::setDescription);
                    Optional.of(role.isActive()).ifPresent(existingRole::setActive);
                    return roleRepo.save(existingRole);
                }
        ).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void deleteRole(String id) {
        roleRepo.deleteById(id);
    }

    public boolean isExists(String id) {
        return roleRepo.existsById(id);
    }
}
