package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<RoleEntity, String> {
}
