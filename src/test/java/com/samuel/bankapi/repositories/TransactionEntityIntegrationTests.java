package com.samuel.bankapi.repositories;

import com.samuel.bankapi.TestDataUtil;
import com.samuel.bankapi.enums.StatusType;
import com.samuel.bankapi.enums.TransactionType;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionEntityIntegrationTests {
    private final TransactionRepo undertest;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final AccountTypeRepo accountTypeRepo;

    @Autowired
    public TransactionEntityIntegrationTests(TransactionRepo undertest, UserRepo userRepo, RoleRepo roleRepo, AccountTypeRepo accountTypeRepo) {
        this.undertest = undertest;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.accountTypeRepo = accountTypeRepo;
    }

}
