package com.samuel.bankapi.services;

import com.samuel.bankapi.exceptions.UserException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        if (userEntity.getPassword() != null) {
            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        }

        if (userEntity.getTransactionPin() != null) {
            userEntity.setTransactionPin(bCryptPasswordEncoder.encode(userEntity.getTransactionPin()));
        }

        // check if userEntity.accountNumber is unique
        if (userEntity.getAccountNumber() != null && userRepo.existsByAccountNumber(userEntity.getAccountNumber())) {
            throw new UserException.UserAlreadyExistsException("Account number already exists");
        }

        userEntity.setCreatedAt(new Date());

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
            Optional<UserEntity> fullUserEntity = userRepo.findByUsername(principal.getUsername());

            // Generate JWT access token
            String accessToken = jwtService.generateToken(loginDto.getUsername());

            UserDto userDto = userMapper.mapTo(fullUserEntity.get());
            fullUserEntity.get().setLastLoginAt(new Date());
            userRepo.save(fullUserEntity.get());
            return new LoginResponseDto(userDto, accessToken);

        }
        return null;
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
        return userRepo.existsById(id) || userRepo.existsByAccountNumber(id);
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

    public UserEntity getUser(String accountNumber) {
        System.out.println("Account Number: " + accountNumber);
        return userRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
    }

    public UserEntity getUserById(String id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
    }

    public UserEntity getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
    }

    public double decreaseBalance(String id, double amount) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
        userEntity.setBalance(userEntity.getBalance() - amount);
        userRepo.save(userEntity);
        return userEntity.getBalance();
    }

    public double increaseBalance(String id, double amount) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
        userEntity.setBalance(userEntity.getBalance() + amount);
        userRepo.save(userEntity);
        return userEntity.getBalance();
    }

    public UserEntity completeRegistration(UserEntity userEntity) {
        if (userEntity.getPassword() != null) {
            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        }

        if (userEntity.getTransactionPin() != null) {
            userEntity.setTransactionPin(bCryptPasswordEncoder.encode(userEntity.getTransactionPin()));
        }


        return userRepo.findByAccountNumber(userEntity.getAccountNumber()).map(
                existingUser ->  {
                    Optional.ofNullable(userEntity.getFullName()).ifPresent(existingUser::setFullName);
                    Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
                    Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
                    Optional.ofNullable(userEntity.getTransactionPin()).ifPresent(existingUser::setTransactionPin);
                    return userRepo.save(existingUser);
                }
        ).orElseThrow(() -> new UserException.UserNotFoundException("UserEntity not found"));
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
    }
}