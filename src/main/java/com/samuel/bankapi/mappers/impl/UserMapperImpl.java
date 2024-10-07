package com.samuel.bankapi.mappers.impl;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto>  {
    private final ModelMapper modelMapper;

    public  UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
