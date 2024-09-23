package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, String> {
    User findByUsername(String username);
}
