package com.samuel.bankapi.controllers;

import com.samuel.bankapi.exceptions.UserException;
import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.LoginDto;
import com.samuel.bankapi.models.dto.LoginResponseDto;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserCtrl {
    private UserService userService;

    private final Mapper<UserEntity, UserDto> userMapper;

    public UserCtrl(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserEntity> userEntities = userService.getUsers();
        List<UserDto> userDtos = userEntities.stream().map(userMapper::mapTo).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<>(userService.registerUser(userEntity), HttpStatus.CREATED);
    }

    @GetMapping("/register/validate-username/{username}")
    public ResponseEntity<?> validateUsername(@PathVariable String username) {
        try {
            boolean isUsernameValid = userService.isUsernameValid(username);

            if (!isUsernameValid) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/register/validate-email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable String email) {
        try {
            boolean isEmailValid = userService.isEmailValid(email);

            if (!isEmailValid) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/register/validate-phone-number/{phoneNumber}")
    public ResponseEntity<?> validatePhoneNumber(@PathVariable String phoneNumber) {
        try {
            boolean isPhoneNumberValid = userService.isPhoneNumberValid(phoneNumber);

            if (!isPhoneNumberValid) {
                return new ResponseEntity<>("Phone Number already exists", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            LoginResponseDto loginResponseDto = userService.loginUser(loginDto, userMapper);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println("Bad exception");
            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserEntity userEntity) {
        try {
            UserEntity updatedUserEntity = userService.updateUser(id, userEntity);
            UserDto updatedUserDto = userMapper.mapTo(updatedUserEntity);
            return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
        }
        catch (UserException.UserNotFoundException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/complete-registration")
    public ResponseEntity<?> completeRegistration(@RequestBody UserEntity userEntity) {
        try {
            System.out.println("user entity" + userEntity);
            UserEntity updatedUserEntity = userService.completeRegistration(userEntity);
            UserDto updatedUserDto = userMapper.mapTo(updatedUserEntity);
            return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
        }
        catch (UserException.UserNotFoundException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (!userService.isExists(id)) {
                return new ResponseEntity<>("UserEntity not found", HttpStatus.NOT_FOUND);
            }
            userService.deleteUser(id);
            return new ResponseEntity<>("UserEntity deleted", HttpStatus.NO_CONTENT);
        }
        catch (UserException.UserNotFoundException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            if (username.equals("me")) {
                UserEntity userEntity  = userService.getCurrentUser();
                UserDto userDto = userMapper.mapTo(userEntity);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            }
            UserEntity userEntity = userService.getUserByUsername(username);
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}




