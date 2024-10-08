package com.samuel.bankapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.bankapi.TestDataUtil;
import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.AccountTypeService;
import com.samuel.bankapi.services.RoleService;
import com.samuel.bankapi.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTests {
    private MockMvc mockMvc;
    private UserService userService;
    private RoleService roleService;
    private AccountTypeService accountTypeService;
    private Mapper<UserEntity, UserDto> userMapper;
    private ObjectMapper objectMapper;


    @Autowired
    public TransactionControllerIntegrationTests(MockMvc mockMvc, UserService userService, RoleService roleService, AccountTypeService accountTypeService, ObjectMapper objectMapper, Mapper<UserEntity, UserDto> userMapper) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.roleService = roleService;
        this.accountTypeService = accountTypeService;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }
}
