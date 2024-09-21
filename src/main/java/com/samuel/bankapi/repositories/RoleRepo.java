package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Role, String> {
}
