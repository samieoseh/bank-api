package com.samuel.bankapi.repositories;

import com.samuel.bankapi.TestDataUtil;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserEntityIntegrationTests {
    private final UserRepo undertest;
    private final RoleRepo roleRepo;
    private final AccountTypeRepo accountTypeRepo;

    @Autowired
    public UserEntityIntegrationTests(UserRepo undertest, RoleRepo roleRepo, AccountTypeRepo accountTypeRepo) {
        this.roleRepo = roleRepo;
        this.accountTypeRepo = accountTypeRepo;
        this.undertest = undertest;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        RoleEntity role = roleRepo.save(TestDataUtil.createRoleEntity("User"));
        AccountTypeEntity accountType = accountTypeRepo.save(TestDataUtil.createAccountTypeEntity("Savings"));
        UserEntity user = TestDataUtil.createUserEntity("testuser", "08012345678", "testuser@gmail.com");
        user.setUserRole(role);
        user.setAccountType(accountType);

        undertest.save(user);

        Optional<UserEntity> result = undertest.findByUsername("testuser");
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testThatUserCanBeUpdatedAndRecalled() {
        RoleEntity role = roleRepo.save(TestDataUtil.createRoleEntity("User"));
        AccountTypeEntity accountType = accountTypeRepo.save(TestDataUtil.createAccountTypeEntity("Savings"));
        UserEntity user = TestDataUtil.createUserEntity("testuser", "08012345678", "testuser@gmail.com");
        user.setUserRole(role);
        user.setAccountType(accountType);
        undertest.save(user);

        // update the user fullName
        user.setFullName("testuser updated");
        UserEntity updatedUserEntity = undertest.save(user);

        Optional<UserEntity> result = undertest.findById(updatedUserEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("testuser updated");
    }

    @Test
    public void testThatUserCanBeDeleted() {
        RoleEntity role = roleRepo.save(TestDataUtil.createRoleEntity("User"));
        AccountTypeEntity accountType = accountTypeRepo.save(TestDataUtil.createAccountTypeEntity("Savings"));
        UserEntity user = TestDataUtil.createUserEntity("testuser", "08012345678", "testuser@gmail.com");
        user.setUserRole(role);
        user.setAccountType(accountType);
        UserEntity savedUserEntity = undertest.save(user);

        undertest.delete(user);

        Optional<UserEntity> result = undertest.findById(savedUserEntity.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        RoleEntity role = TestDataUtil.createRoleEntity("User");
        RoleEntity savedRole = roleRepo.save(role);
        AccountTypeEntity accountType = TestDataUtil.createAccountTypeEntity("Savings");
        AccountTypeEntity savedAccountType = accountTypeRepo.save(accountType);

        for (int i = 0; i < 5; i++) {
            UserEntity user = TestDataUtil.createUserEntity("testuser" + i, "08012345678" + i, "testuser" + i + "@gmail.com");
            user.setUserRole(savedRole);
            user.setAccountType(savedAccountType);
            undertest.save(user);
        }

        assertThat(undertest.findAll()).hasSize(5);

    }
}
