package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.entities.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepo extends CrudRepository<Token, String> {
    Optional<Token> findByEmailAddress(String emailAddress);
    @Transactional
    void deleteByEmailAddress(String emailAddress);
}
