package com.samuel.bankapi.services;

import com.samuel.bankapi.models.LoginDto;
import com.samuel.bankapi.models.User;
import com.samuel.bankapi.models.UserPrincipal;
import com.samuel.bankapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        System.out.println(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // hash password
        return userRepo.save(user);
    }


    public LoginDto loginUser(User user) throws Exception {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        System.out.println("is authenticated: " + authentication.isAuthenticated());
        // If authentication is successful
        if (authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User fullUser = userRepo.findByUsername(principal.getUsername());

            // Generate JWT access token
            String accessToken = jwtService.generateToken(user.getUsername());
            System.out.println("Access token: " + accessToken);

            // Build and return LoginDto with user details and access token
            return new LoginDto(
                    fullUser.getId(),
                    fullUser.getUsername(),
                    fullUser.getEmail(),
                    fullUser.getFullName(),
                    accessToken
            );
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

}
