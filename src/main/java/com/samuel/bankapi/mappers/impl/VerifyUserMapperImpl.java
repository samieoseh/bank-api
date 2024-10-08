package com.samuel.bankapi.mappers.impl;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.VerifyUserDto;
import com.samuel.bankapi.models.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VerifyUserMapperImpl implements Mapper<UserEntity, VerifyUserDto>  {
    private final ModelMapper modelMapper;

    public VerifyUserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VerifyUserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, VerifyUserDto.class);
    }

    @Override
    public UserEntity mapFrom(VerifyUserDto verifyUserDto) {
        return modelMapper.map(verifyUserDto, UserEntity.class);
    }
}
