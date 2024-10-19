package com.samuel.bankapi;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.models.entities.RoleEntity;
import com.samuel.bankapi.models.entities.UserEntity;

public class TestDataUtil {
    private TestDataUtil() {
    }

    public static UserEntity createUserEntity(String username, String phoneNumber, String email) {
        return UserEntity.builder()
                .username(username)
                .password("12345678")
                .transactionPin("1234")
                .email(email)
                .phoneNumber(phoneNumber)
                .fullName("Test User")
                .address("123, Test Street, Test Cit")
                .accountNumber(phoneNumber.substring(1))
                .balance(10000.0)
                .active(true)
                .emailVerified(true)
                .phoneNumberVerified(true)
                .failedLoginAttempts(0)
                .build();
    }

    public static RoleEntity createRoleEntity(String role) {
        return RoleEntity.builder()
                .roleName(role)
                .description("Test Role")
                .active(true)
                .build();
    }

    public static AccountTypeEntity createAccountTypeEntity(String type) {
        return AccountTypeEntity.builder()
                .typeName(type)
                .description("Test Account Type")
                .interestRate(0.0)
                .minimumBalance(0.0)
                .build();
    }
}
