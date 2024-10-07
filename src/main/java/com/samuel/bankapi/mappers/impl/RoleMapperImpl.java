package com.samuel.bankapi.mappers.impl;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.RoleDto;
import com.samuel.bankapi.models.entities.RoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperImpl implements Mapper<RoleEntity, RoleDto> {

    private final  ModelMapper modelMapper;

    public RoleMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public RoleDto mapTo(RoleEntity roleEntity) {
        return modelMapper.map(roleEntity, RoleDto.class);
    }

    @Override
    public RoleEntity mapFrom(RoleDto roleDto) {
        return modelMapper.map(roleDto, RoleEntity.class);
    }
}
