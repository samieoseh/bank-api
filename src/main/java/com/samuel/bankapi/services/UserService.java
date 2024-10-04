package com.samuel.bankapi.services;

import com.samuel.bankapi.UserException;
import com.samuel.bankapi.models.dto.LoginDto;
import com.samuel.bankapi.models.entities.User;
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

    public List<User> getUsers() {
        return StreamSupport.stream(userRepo.findAll().spliterator(), false).toList();
    }

    public User registerUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setTransactionPin(bCryptPasswordEncoder.encode(user.getTransactionPin()));// hash password

        // check if user.accountNumber is unique
        if (userRepo.existsByAccountNumber(user.getAccountNumber())) {
            throw new UserException.UserAlreadyExistsException("Account number already exists");
        }
        return userRepo.save(user);
    }


    public LoginDto loginUser(User user) throws Exception {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // If authentication is successful
        if (authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User fullUser = userRepo.findByUsername(principal.getUsername());

            // Generate JWT access token
            String accessToken = jwtService.generateToken(user.getUsername());

            // Build and return LoginDto with user details and access token
            return new LoginDto(
                    fullUser.getId(),
                    fullUser.getUsername(),
                    fullUser.getEmail(),
                    fullUser.getFullName(),
                    fullUser.getBalance(),
                    fullUser.getAccountType(),
                    fullUser.getUserRole(),
                    fullUser.getAccountNumber(),
                    fullUser.getPhoneNumber(),
                    fullUser.isActive(),
                    accessToken
            );
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    public User updateUser(String id, User user) {
        user.setId(id);

        if  (user.getPassword() != null) {
            // hash the password if it exists
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        if (user.getTransactionPin() != null) {
            // hash the transaction pin if it exists
            user.setTransactionPin(bCryptPasswordEncoder.encode(user.getTransactionPin()));
        }

        return userRepo.findById(id).map(
                existingUser -> {
                    Optional.ofNullable(user.getUsername()).ifPresent(existingUser::setUsername);
                    Optional.ofNullable(user.getPassword()).ifPresent(existingUser::setPassword);
                    Optional.ofNullable(user.getTransactionPin()).ifPresent(existingUser::setTransactionPin);
                    Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(user.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);
                    Optional.ofNullable(user.getFullName()).ifPresent(existingUser::setFullName);
                    Optional.ofNullable(user.getAddress()).ifPresent(existingUser::setAddress);
                    Optional.ofNullable(user.getAccountNumber()).ifPresent(existingUser::setAccountNumber);
                    Optional.ofNullable(user.getBalance()).ifPresent(existingUser::setBalance);
                    Optional.ofNullable(user.getAccountType()).ifPresent(existingUser::setAccountType);
                    Optional.ofNullable(user.getUserRole()).ifPresent(existingUser::setUserRole);
                    Optional.of(user.isActive()).ifPresent(existingUser::setActive);
                    Optional.ofNullable(user.getLastLoginAt()).ifPresent(existingUser::setLastLoginAt);
                    Optional.ofNullable(user.getCreatedAt()).ifPresent(existingUser::setCreatedAt);
                    Optional.ofNullable(user.getProfilePictureUrl()).ifPresent(existingUser::setProfilePictureUrl);
                    Optional.of(user.isEmailVerified()).ifPresent(existingUser::setEmailVerified);
                    Optional.of(user.isPhoneNumberVerified()).ifPresent(existingUser::setPhoneNumberVerified);
                    Optional.of(user.isTwoFactorAuthEnabled()).ifPresent(existingUser::setTwoFactorAuthEnabled);
                    Optional.of(user.getFailedLoginAttempts()).ifPresent(existingUser::setFailedLoginAttempts);
                    Optional.ofNullable(user.getAccountLockedUntil()).ifPresent(existingUser::setAccountLockedUntil);

                    return userRepo.save(existingUser);
                }
        ).orElseThrow(() -> new UserException.UserNotFoundException("User not found"));
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public boolean isExists(String id) {
        return userRepo.existsById(id);
    }
}