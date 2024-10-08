package com.samuel.bankapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.bankapi.enums.ActionType;
import com.samuel.bankapi.exceptions.TransactionException;
import com.samuel.bankapi.exceptions.UserException;
import com.samuel.bankapi.enums.StatusType;
import com.samuel.bankapi.models.UserPrincipal;
import com.samuel.bankapi.models.entities.TransactionAuditEntity;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.repositories.TransactionRepo;
import com.samuel.bankapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionAuditService transactionAuditService;

    @Autowired
    private  UserService userService;

    public boolean isBalanceSufficient(double amount) {
        // get the current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        // check if the userEntity balance is greater than the amount
        return userEntity.getBalance() >= amount;
    }

    private boolean usersActive(TransactionEntity createdTransactionEntity) {
        if (!createdTransactionEntity.getSender().isActive() || !createdTransactionEntity.getReciever().isActive()) {
            throw new TransactionException.InvalidTransactionException("Sender or receiver not an active account");
        }

        return true;
    }

    private boolean validateTransactionInput(TransactionEntity transactionEntity) {
        // check if the sender and receiver are the same
        if (transactionEntity.getSender().getId().equals(transactionEntity.getReciever().getId())) {
            throw new TransactionException.InvalidTransactionException("Sender and receiver cannot be the same");
        }

        // check if the amount is greater than 0
        if (transactionEntity.getAmount() <= 0) {
            throw new TransactionException.InvalidTransactionException("Amount must be greater than 0");
        }

        // check if the sender has sufficient balance
        if (!isBalanceSufficient(transactionEntity.getAmount())) {
            throw new TransactionException.InvalidTransactionException("Insufficient funds");
        }
        return true;
    }

    private boolean validateTransactionPin(String transactionPin) {
        // get the current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        // check if the transaction pin is correct
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(transactionPin, userEntity.getTransactionPin())) {
           throw new TransactionException.InvalidCredentials("Invalid transaction pin");
        }

        return true;
    }

    private Map<String, Double> processTransaction(TransactionEntity createdTransactionEntity) {
        // Update the sender's and receiver's balances
        UserEntity sender = createdTransactionEntity.getSender();
        UserEntity receiver = createdTransactionEntity.getReciever();

        double updatedSenderBalance = userService.decreaseBalance(sender.getId(), createdTransactionEntity.getAmount());
        double updatedReceiverBalance = userService.increaseBalance(receiver.getId(), createdTransactionEntity.getAmount());

        // Update the transaction status to COMPLETED
        createdTransactionEntity.setStatus(StatusType.COMPLETED);
        transactionRepo.save(createdTransactionEntity);

        Map<String, Double> balances = new HashMap<>();
        balances.put("senderBalance", updatedSenderBalance);
        balances.put("receiverBalance", updatedReceiverBalance);

        return balances;
    }

    public TransactionEntity createTransaction(TransactionEntity transactionEntity) throws JsonProcessingException {
        // transaction initiation
        transactionEntity.setTransactionDate(new Date());
        transactionEntity.setStatus(StatusType.PENDING);

        TransactionEntity createdTransactionEntity = transactionRepo.save(transactionEntity);

        // log the audit for transaction initiation
        createdTransactionEntity.setStatus(StatusType.INITIATED);

        TransactionAuditEntity transactionAuditEntity = TransactionAuditEntity.builder()
                .transaction(createdTransactionEntity)
                .oldData(null)
                .newData(objectMapper.writeValueAsString(createdTransactionEntity))
                .action(ActionType.CREATE)
                .performedAt(new Date())
                .performedBy(createdTransactionEntity.getSender())
                .build();

        transactionAuditService.createTransactionAudit(transactionAuditEntity);

        // starting validating
        // log audit old Data {status: INITIATED} to new Data {status: VALIDATING}
        TransactionAuditEntity transactionAuditValidateInputEntity = TransactionAuditEntity.builder()
                .transaction(createdTransactionEntity)
                .oldData(objectMapper.writeValueAsString(objectMapper.writeValueAsString(createdTransactionEntity)))
                .newData(objectMapper.writeValueAsString(Map.of("status", "VALIDATING")))
                .action(ActionType.VALIDATE)
                .performedBy(createdTransactionEntity.getSender())
                .performedAt(new Date())
                .build();

        transactionAuditService.createTransactionAudit(transactionAuditValidateInputEntity);

        // validate incoming transactions input
        boolean isTransactionInputValid = validateTransactionInput(createdTransactionEntity);

        if (isTransactionInputValid) {
            // log audit old Data {status: VALIDATING} to new Data {status: VALIDATED}
            TransactionAuditEntity transactionAuditValidateInputEntity2 = TransactionAuditEntity.builder()
                    .transaction(createdTransactionEntity)
                    .oldData(objectMapper.writeValueAsString(Map.of("status", "VALIDATING")))
                    .newData(objectMapper.writeValueAsString(Map.of("status", "VALIDATED")))
                    .action(ActionType.VALIDATE)
                    .performedBy(createdTransactionEntity.getSender())
                    .performedAt(new Date())
                    .build();
            transactionAuditService.createTransactionAudit(transactionAuditValidateInputEntity2);
        }

        // starting authentication
        // log audit old Data {status: VALIDATED} to new Data {status: AUTHENTICATING}
        TransactionAuditEntity transactionAuditAuthenticateEntity = TransactionAuditEntity.builder()
                .transaction(createdTransactionEntity)
                .oldData(objectMapper.writeValueAsString(Map.of("status", "VALIDATED")))
                .newData(objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATING")))
                .action(ActionType.AUTHENTICATE)
                .performedBy(createdTransactionEntity.getSender())
                .performedAt(new Date())
                .build();

        transactionAuditService.createTransactionAudit(transactionAuditAuthenticateEntity);

         // authenticate transaction
        boolean isTransactionAuthenticated = validateTransactionPin(createdTransactionEntity.getTransactionPin());

       if (isTransactionAuthenticated) {
            // log audit old Data {status: AUTHENTICATING} to new Data {status: AUTHENTICATED}
            TransactionAuditEntity transactionAuditAuthenticateEntity2 = TransactionAuditEntity.builder()
                    .transaction(createdTransactionEntity)
                    .oldData(objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATING")))
                    .newData(objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATED")))
                    .action(ActionType.AUTHENTICATE)
                    .performedBy(createdTransactionEntity.getSender())
                    .performedAt(new Date())
                    .build();
            transactionAuditService.createTransactionAudit(transactionAuditAuthenticateEntity2);
        }

       //  pre-processing
        // log audit old Data {status: AUTHENTICATED} to new Data {status: PRE-PROCESSING}
        TransactionAuditEntity transactionAuditPreProcessEntity = TransactionAuditEntity.builder()
                .transaction(createdTransactionEntity)
                .oldData(objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATED")))
                .newData(objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSING")))
                .action(ActionType.PRE_PROCESS)
                .performedBy(createdTransactionEntity.getSender())
                .performedAt(new Date())
                .build();

        transactionAuditService.createTransactionAudit(transactionAuditPreProcessEntity);

        // check if both account are active
        // check if sender has enough balance
        boolean isTransactionReadyForProcessing = isBalanceSufficient(createdTransactionEntity.getAmount()) && usersActive(
                createdTransactionEntity
        );

        if (isTransactionReadyForProcessing) {
            // log audit old Data {status: PRE-PROCESSING} to new Data {status: PROCESSING}
            TransactionAuditEntity transactionAuditPreProcessEntity2 = TransactionAuditEntity.builder()
                    .transaction(createdTransactionEntity)
                    .oldData(objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSING")))
                    .newData(objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSED")))
                    .action(ActionType.PRE_PROCESS)
                    .performedBy(createdTransactionEntity.getSender())
                    .performedAt(new Date())
                    .build();
            transactionAuditService.createTransactionAudit(transactionAuditPreProcessEntity2);
        }

        // processing
        // log audit old Data {status: PROCESSING} to new Data {status: PROCESSED}
        TransactionAuditEntity transactionAuditProcessEntity = TransactionAuditEntity.builder()
                .transaction(createdTransactionEntity)
                .oldData(objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSED")))
                .newData(objectMapper.writeValueAsString(Map.of("status", "PROCESSING")))
                .action(ActionType.PROCESS)
                .performedBy(createdTransactionEntity.getSender())
                .performedAt(new Date())
                .build();

        transactionAuditService.createTransactionAudit(transactionAuditProcessEntity);

        // update the sender and receiver balance
        // should be transactional and atomic
        try {
            Map<String, Double> balances = processTransaction(createdTransactionEntity);
            TransactionAuditEntity transactionAuditProcessEntity2 = TransactionAuditEntity.builder()
                    .transaction(createdTransactionEntity)
                    .oldData(objectMapper.writeValueAsString(Map.of("status", "PROCESSING")))
                    .newData(objectMapper.writeValueAsString(Map.of("status", "PROCESSED", "senderBalance", balances.get("senderBalance"), "receiverBalance", balances.get("receiverBalance"))))
                    .action(ActionType.PROCESS)
                    .performedBy(createdTransactionEntity.getSender())
                    .performedAt(new Date())
                    .build();

            transactionAuditService.createTransactionAudit(transactionAuditProcessEntity2);

        } catch (Exception e) {
            throw new TransactionException.BadTransaction("An error occurred during transaction");
        }


        return createdTransactionEntity;
    }
}
