package com.samuel.bankapi.services;

import com.samuel.bankapi.models.entities.RoleEntity;
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

    public List<RoleEntity> getRoles() {
        return StreamSupport.stream(roleRepo.findAll().spliterator(), false).toList();
    }

    public RoleEntity createRole(RoleEntity roleEntity) {
        return roleRepo.save(roleEntity);
    }

    public RoleEntity updateRole(String id, RoleEntity roleEntity) {
        roleEntity.setId(id);
        System.out.println("RoleEntity: " + roleEntity);
        return roleRepo.findById(id).map(
                existingRole -> {
                    Optional.ofNullable(roleEntity.getRoleName()).ifPresent(existingRole::setRoleName);
                    Optional.ofNullable(roleEntity.getDescription()).ifPresent(existingRole::setDescription);
                    Optional.of(roleEntity.isActive()).ifPresent(existingRole::setActive);
                    return roleRepo.save(existingRole);
                }
        ).orElseThrow(() -> new RuntimeException("RoleEntity not found"));
    }

    public void deleteRole(String id) {
        roleRepo.deleteById(id);
    }

    public boolean isExists(String id) {
        return roleRepo.existsById(id);
    }

    public boolean isExistsByName(String roleName) {
        return roleRepo.existsByRoleName(roleName);
    }

    public RoleEntity getRole(String id) {
        return roleRepo.findById(id).orElse(null);
    }
}
