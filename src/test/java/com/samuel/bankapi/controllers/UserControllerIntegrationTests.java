package com.samuel.bankapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.bankapi.TestDataUtil;
import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.LoginDto;
import com.samuel.bankapi.models.dto.LoginResponseDto;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.AccountTypeService;
import com.samuel.bankapi.services.RoleService;
import com.samuel.bankapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    private MockMvc mockMvc;
    private UserService userService;
    private RoleService roleService;
    private AccountTypeService accountTypeService;
    private Mapper<UserEntity, UserDto> userMapper;
    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserService userService, RoleService roleService, AccountTypeService accountTypeService, ObjectMapper objectMapper, Mapper<UserEntity, UserDto> userMapper) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.roleService = roleService;
        this.accountTypeService = accountTypeService;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    // @Test
    // public void testThatCreateUserReturnsHttpStatus201Created() throws Exception
    // {
    // RoleEntity role =
    // roleService.createRole(TestDataUtil.createRoleEntity("User"));
    // AccountTypeEntity accountType =
    // accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
    // UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
    // "user1@gmail.com");
    // userEntity.setUserRole(role);
    // userEntity.setAccountType(accountType);

// String userEntityJson = objectMapper.writeValueAsString(userEntity);
// mockMvc.perform(
// MockMvcRequestBuilders.post("/api/users/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content(userEntityJson)
// ).andExpect(
// MockMvcResultMatchers.status().isCreated()
// );
// }

// @Test
// public void testThatCreateUserReturnsTheCorrectResponse() throws Exception {
// RoleEntity role =
// roleService.createRole(TestDataUtil.createRoleEntity("User"));
// AccountTypeEntity accountType =
// accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
// UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
// "user1@gmail.com");
// userEntity.setUserRole(role);
// userEntity.setAccountType(accountType);

// String userEntityJson = objectMapper.writeValueAsString(userEntity);

// mockMvc.perform(
// MockMvcRequestBuilders.post("/api/users/register")
// .contentType(MediaType.APPLICATION_JSON)
// .content(userEntityJson)
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.username").value("user1")
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.phoneNumber").value("08102867345")
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.email").value("user1@gmail.com")
// );
// }

// @Test
// public void testThatUpdateUserReturnsHttpStatus200OK() throws Exception {
// RoleEntity role =
// roleService.createRole(TestDataUtil.createRoleEntity("User"));
// AccountTypeEntity accountType =
// accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
// UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
// "user1@gmail.con");
// userEntity.setUserRole(role);
// userEntity.setAccountType(accountType);

// userService.registerUser(userEntity);
// LoginDto loginDto = new LoginDto("12345678", "user1");
// LoginResponseDto loggedInUser = userService.loginUser(loginDto, userMapper);

// String accessToken = loggedInUser.getAccessToken();
// userEntity.setPhoneNumber("08102867346");

// String userEntityJson = objectMapper.writeValueAsString(userEntity);
// mockMvc.perform(
// MockMvcRequestBuilders.patch("/api/users/" + userEntity.getId())
// .contentType(MediaType.APPLICATION_JSON)
// .header("Authorization", "Bearer " + accessToken)
// .content(userEntityJson)
// ).andExpect(
// MockMvcResultMatchers.status().isOk()
// );
// }

// @Test
// public void testThatUpdateUserReturnsHttpStatus401Unauthorized() throws
// Exception {
// RoleEntity role =
// roleService.createRole(TestDataUtil.createRoleEntity("User"));
// AccountTypeEntity accountType =
// accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
// UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
// "user1@gmail.com");
// userEntity.setUserRole(role);
// userEntity.setAccountType(accountType);
// userService.registerUser(userEntity);

// userEntity.setPhoneNumber("08102867346");
// String userEntityJson = objectMapper.writeValueAsString(userEntity);


// mockMvc.perform(
// MockMvcRequestBuilders.patch("/api/users/" + userEntity.getId())
// .contentType(MediaType.APPLICATION_JSON)
// .content(userEntityJson)
// ).andExpect(
// MockMvcResultMatchers.status().isUnauthorized()
// );
// }

// @Test
// public void testThatUpdateUserReturnsTheCorrectResponse() throws Exception {
// RoleEntity role =
// roleService.createRole(TestDataUtil.createRoleEntity("User"));
// AccountTypeEntity accountType =
// accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
// UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
// "user1@gmail.com");
// userEntity.setUserRole(role);
// userEntity.setAccountType(accountType);

// userService.registerUser(userEntity);
// LoginDto loginDto = new LoginDto("12345678", "user1");
// LoginResponseDto loggedInUser = userService.loginUser(loginDto, userMapper);

// String accessToken = loggedInUser.getAccessToken();
// userEntity.setPhoneNumber("08102867346");
// String userEntityJson = objectMapper.writeValueAsString(userEntity);

// mockMvc.perform(
// MockMvcRequestBuilders.patch("/api/users/" + userEntity.getId())
// .contentType(MediaType.APPLICATION_JSON)
// .header("Authorization", "Bearer " + accessToken)
// .content(userEntityJson)
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.username").value("user1")
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.phoneNumber").value("08102867346")
// ).andExpect(
// MockMvcResultMatchers.jsonPath("$.email").value("user1@gmail.com")
// );
// }

// @Test
// public void testThatDeleteUserReturnsHttpStatus204NoContent() throws
// Exception {
// RoleEntity role =
// roleService.createRole(TestDataUtil.createRoleEntity("User"));
// AccountTypeEntity accountType =
// accountTypeService.createAccountType(TestDataUtil.createAccountTypeEntity("Savings"));
// UserEntity userEntity = TestDataUtil.createUserEntity("user1", "08102867345",
// "user1@gmail.com");

// userEntity.setUserRole(role);
// userEntity.setAccountType(accountType);

// userService.registerUser(userEntity);
// LoginDto loginDto = new LoginDto("12345678", "user1");
// LoginResponseDto loggedInUser = userService.loginUser(loginDto, userMapper);

// String accessToken = loggedInUser.getAccessToken();

// mockMvc.perform(
// MockMvcRequestBuilders.delete("/api/users/" + userEntity.getId())
// .contentType(MediaType.APPLICATION_JSON)
// .header("Authorization", "Bearer " + accessToken)
// ).andExpect(
// MockMvcResultMatchers.status().isNoContent()
// );
// }
}