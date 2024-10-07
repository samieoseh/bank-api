package com.samuel.bankapi.services;

import com.samuel.bankapi.UserException;
import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.LoginDto;
import com.samuel.bankapi.models.dto.LoginResponseDto;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.models.UserPrincipal;
import com.samuel.bankapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserEntity> getUsers() {
        return StreamSupport.stream(userRepo.findAll().spliterator(), false).toList();
    }

    public UserEntity registerUser(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setTransactionPin(bCryptPasswordEncoder.encode(userEntity.getTransactionPin()));// hash password

        // check if userEntity.accountNumber is unique
        if (userRepo.existsByAccountNumber(userEntity.getAccountNumber())) {
            throw new UserException.UserAlreadyExistsException("Account number already exists");
        }
        return userRepo.save(userEntity);
    }


    public LoginResponseDto loginUser(LoginDto loginDto, Mapper<UserEntity, UserDto> userMapper) throws Exception {
        // Authenticate the userEntity
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // If authentication is successful
        if (authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            UserEntity fullUserEntity = userRepo.findByUsername(principal.getUsername());

            // Generate JWT access token
            String accessToken = jwtService.generateToken(loginDto.getUsername());

            UserDto userDto = userMapper.mapTo(fullUserEntity);
            return new LoginResponseDto(userDto, accessToken);

        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    public UserEntity updateUser(String id, UserEntity userEntity) {
        userEntity.setId(id);

        if  (userEntity.getPassword() != null) {
            // hash the password if it exists
            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        }

        if (userEntity.getTransactionPin() != null) {
            // hash the transaction pin if it exists
            userEntity.setTransactionPin(bCryptPasswordEncoder.encode(userEntity.getTransactionPin()));
        }

        return userRepo.findById(id).map(
                existingUser -> {
                    Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
                    Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
                    Optional.ofNullable(userEntity.getTransactionPin()).ifPresent(existingUser::setTransactionPin);
                    Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(userEntity.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);
                    Optional.ofNullable(userEntity.getFullName()).ifPresent(existingUser::setFullName);
                    Optional.ofNullable(userEntity.getAddress()).ifPresent(existingUser::setAddress);
                    Optional.ofNullable(userEntity.getAccountNumber()).ifPresent(existingUser::setAccountNumber);
                    Optional.ofNullable(userEntity.getBalance()).ifPresent(existingUser::setBalance);
                    Optional.ofNullable(userEntity.getAccountType()).ifPresent(existingUser::setAccountType);
                    Optional.ofNullable(userEntity.getUserRole()).ifPresent(existingUser::setUserRole);
                    Optional.of(userEntity.isActive()).ifPresent(existingUser::setActive);
                    Optional.ofNullable(userEntity.getLastLoginAt()).ifPresent(existingUser::setLastLoginAt);
                    Optional.ofNullable(userEntity.getCreatedAt()).ifPresent(existingUser::setCreatedAt);
                    Optional.ofNullable(userEntity.getProfilePictureUrl()).ifPresent(existingUser::setProfilePictureUrl);
                    Optional.of(userEntity.isEmailVerified()).ifPresent(existingUser::setEmailVerified);
                    Optional.of(userEntity.isPhoneNumberVerified()).ifPresent(existingUser::setPhoneNumberVerified);
                    Optional.of(userEntity.isTwoFactorAuthEnabled()).ifPresent(existingUser::setTwoFactorAuthEnabled);
                    Optional.of(userEntity.getFailedLoginAttempts()).ifPresent(existingUser::setFailedLoginAttempts);
                    Optional.ofNullable(userEntity.getAccountLockedUntil()).ifPresent(existingUser::setAccountLockedUntil);

                    return userRepo.save(existingUser);
                }
        ).orElseThrow(() -> new UserException.UserNotFoundException("UserEntity not found"));
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public boolean isExists(String id) {
        return userRepo.existsById(id);
    }

    public boolean isUsernameValid(String username) {
        return !userRepo.existsByUsername(username);
    }

    public boolean isEmailValid(String email) {
        return !userRepo.existsByEmail(email);
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return !userRepo.existsByPhoneNumber(phoneNumber);
    }
}