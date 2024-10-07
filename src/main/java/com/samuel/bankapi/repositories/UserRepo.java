package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
    boolean existsByAccountNumber(String accountNumber);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
