package com.samuel.bankapi.controllers;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.RoleDto;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleCtrl {

    private final RoleService roleService;

    Mapper<RoleEntity, RoleDto> roleMapper;

    public RoleCtrl(RoleService roleService, Mapper<RoleEntity, RoleDto> roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<RoleDto>> getRoles() {
        List<RoleEntity> roleEntities = roleService.getRoles();
        List<RoleDto> roleDtos = roleEntities.stream().map(roleMapper::mapTo).toList();

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleEntity roleEntity = roleMapper.mapFrom(roleDto);
        RoleEntity createdRoleEntity = roleService.createRole(roleEntity);
        RoleDto createdRoleDto = roleMapper.mapTo(createdRoleEntity);
        return new ResponseEntity<>(createdRoleDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable String id, @RequestBody RoleDto roleDto) {

        if (roleService.isExists(id)) {
            RoleEntity roleEntity = roleMapper.mapFrom(roleDto);
            RoleEntity updatedRoleEntity = roleService.updateRole(id, roleEntity);
            RoleDto updatedRoleDto = roleMapper.mapTo(updatedRoleEntity);
            return new ResponseEntity<>(updatedRoleDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable String id) {
        if(roleService.isExists(id)) {
            roleService.deleteRole(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
