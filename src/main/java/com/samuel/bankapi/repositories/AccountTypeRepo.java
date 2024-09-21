package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.AccountType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepo  extends CrudRepository<AccountType, String> {

}
