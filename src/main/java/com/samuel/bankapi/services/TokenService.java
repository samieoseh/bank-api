package com.samuel.bankapi.services;

import com.samuel.bankapi.models.dto.VerifyDto;
import com.samuel.bankapi.models.entities.Token;
import com.samuel.bankapi.repositories.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private TokenRepo tokenRepo;

    private static final int TOKEN_LENGTH = 6;
    private SecureRandom secureRandom = new SecureRandom();

    private String createToken() {
        int number = 100000 + secureRandom.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(number);
    }

    public Token createAndStoreToken(String email) {
        // Remove any existing token for the email
        tokenRepo.deleteByEmailAddress(email);

        String tokenStr = createToken();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // Token valid for 10 minutes

        Token token = new Token();
        token.setToken(tokenStr);
        token.setEmailAddress(email);
        token.setExpirationTime(expirationTime);

        return tokenRepo.save(token);
    }

    public  Boolean verifyToken(VerifyDto verifyDto) {
        Optional<Token> tokenOptional = tokenRepo.findByEmailAddress(verifyDto.getEmailAddress());
        if (tokenOptional.isEmpty()) {
            return false;
        }
        Token token = tokenOptional.get();

        if (!token.getToken().equals(verifyDto.getToken())) {
            System.out.println("Token mismatch");
            return false;
        }
        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            System.out.println("Token expired");
            return false;
        }

        tokenRepo.deleteByEmailAddress(verifyDto.getEmailAddress());

        return true;
    }
}
