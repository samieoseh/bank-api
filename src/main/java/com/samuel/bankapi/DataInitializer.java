package com.samuel.bankapi;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.repositories.RoleRepo;
import com.samuel.bankapi.repositories.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("running data initializer");
        Optional<RoleEntity> adminRole = roleRepo.findByRoleName("admin");
        if (!adminRole.isPresent()) {
            RoleEntity adminRoleEntity = new RoleEntity();
            adminRoleEntity.setRoleName("admin");
            adminRoleEntity.setDescription("admin description");
            adminRoleEntity.setActive(true);
            roleRepo.save(adminRoleEntity);
        }

        Optional<UserEntity> adminUser = userRepo.findByUsername("admin");

        if (!adminUser.isPresent()) {
            Optional<RoleEntity> savedAdminRole = roleRepo.findByRoleName("admin");

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("admin");
            userEntity.setActive(true);
            userEntity.setEmail("admin@example.com");
            String password = encoder.encode("admin");
            userEntity.setPassword(password);
            userEntity.setAccountNumber("1234567890");
            userEntity.setPhoneNumber("12345678912");
            userEntity.setUserRole(savedAdminRole.get());
            userEntity.setFullName("Admin Admin");
            userEntity.setBalance(1000.00);
            userEntity.setCreatedAt(new Date());
            userRepo.save(userEntity);
        }
    }

}
