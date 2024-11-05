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
import com.samuel.bankapi.models.entities.TransactionReceiptEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.repositories.TransactionReceiptRepo;
import com.samuel.bankapi.repositories.TransactionRepo;
import com.samuel.bankapi.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Service
@Slf4j
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
    private UserService userService;

    @Autowired
    private TransactionReceiptRepo transactionReceiptRepo;

    public boolean isBalanceSufficient(UserEntity userEntity, double amount) {
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
            transactionEntity.setStatus(StatusType.FAILED);
            transactionRepo.save(transactionEntity);
            throw new TransactionException.InvalidTransactionException("Sender and receiver cannot be the same");
        }

        // check if the amount is greater than 0
        if (transactionEntity.getAmount() < 100) {
            transactionEntity.setStatus(StatusType.FAILED);
            transactionRepo.save(transactionEntity);
            throw new TransactionException.InvalidTransactionException("Amount must be greater than 100");
        }

        // check if the sender has sufficient balance
        if (!isBalanceSufficient(transactionEntity.getSender(), transactionEntity.getAmount())) {
            transactionEntity.setStatus(StatusType.FAILED);
            transactionRepo.save(transactionEntity);
            throw new TransactionException.InvalidTransactionException("Insufficient funds");
        }

        // check if the sender id is same as the current user logged in when current
        // user logged has role user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        if (userEntity.getUserRole().getRoleName().equals("user")) {
            if (!transactionEntity.getSender().getId().equals(userEntity.getId())) {
                transactionEntity.setStatus(StatusType.FAILED);
                transactionRepo.save(transactionEntity);
                throw new TransactionException.InvalidTransactionException("Sender must be the authenticated user");
            }
        }

        return true;
    }

    private boolean validateTransactionPin(TransactionEntity transactionEntity) {
        // get the current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        UserEntity userEntity = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        if (userEntity.getUserRole().getRoleName().equals("user")) {
            // check if the transaction pin is correct
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!encoder.matches(transactionEntity.getTransactionPin(), userEntity.getTransactionPin())) {
                transactionEntity.setStatus(StatusType.FAILED);
                transactionRepo.save(transactionEntity);
                throw new TransactionException.InvalidCredentials("Invalid transaction pin");
            }
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

    private double getNonNullBalance(Double balance) {
        if (balance == null) {
            throw new TransactionException.InvalidTransactionException("Balance cannot be null");
        }
        return balance;
    }

    public TransactionEntity createTransaction(TransactionEntity transactionEntity) throws JsonProcessingException {
        // Initialize transaction
        initializeTransaction(transactionEntity);
        log.info(String.format("Initialized transaction:: %s", transactionEntity.getId()));


        // Log audit for transaction initiation
        logTransactionAudit(transactionEntity, null, objectMapper.writeValueAsString(transactionEntity), ActionType.CREATE);
        log.info(String.format("Log transaction audit:: %s, %s", transactionEntity.getId(), ActionType.CREATE));

        // Validate transaction input
        validateTransactionInputAndLogAudit(transactionEntity);
        log.info(String.format("Transaction validated and logged:: %s", transactionEntity.getId()));

        // Authenticate transaction
        authenticateTransactionAndLogAudit(transactionEntity);
        log.info(String.format("Transaction authenticated and logged:: %s", transactionEntity.getId()));

        // Pre-process transaction
        preProcessTransactionAndLogAudit(transactionEntity);
        log.info(String.format("Transaction preprocessed and logged:: %s", transactionEntity.getId()));

        // Process transaction
        processTransactionAndLogAudit(transactionEntity);
        log.info(String.format("Transaction processed and logged:: %s", transactionEntity.getId()));

        transactionEntity.setTransactionDate(new Date());

        // create two transaction receipts for the sender and receiver
        TransactionReceiptEntity senderReceipt = TransactionReceiptEntity.builder()
                .transaction(transactionEntity)
                .transactionType("DEBIT")
                .transactionDate(transactionEntity.getTransactionDate())
                .amount(transactionEntity.getAmount())
                .previousBalance(
                        getNonNullBalance(transactionEntity.getSender().getBalance()) + transactionEntity.getAmount())
                .newBalance(
                        getNonNullBalance(transactionEntity.getSender().getBalance()))
                .user(transactionEntity.getSender())
                .description("Debit transaction")
                .status("COMPLETED")
                .reference(transactionEntity.getId())
                .build();

        TransactionReceiptEntity createdSenderReceipt = transactionReceiptRepo.save(senderReceipt);
        log.info(String.format("Sender TransactionReceipt generated:: %s", createdSenderReceipt.getId()));

        TransactionReceiptEntity receiverReceipt = TransactionReceiptEntity.builder()
                .transaction(transactionEntity)
                .transactionType("CREDIT")
                .transactionDate(transactionEntity.getTransactionDate())
                .amount(transactionEntity.getAmount())
                .previousBalance(getNonNullBalance(transactionEntity.getReciever()
                        .getBalance()) -
                        transactionEntity.getAmount())
                .newBalance(getNonNullBalance(transactionEntity.getReciever()
                        .getBalance()))
                .user(transactionEntity.getReciever())
                .description("Credit transaction")
                .status("COMPLETED")
                .reference(transactionEntity.getId())
                .build();

        TransactionReceiptEntity createdReceiverReceipt = transactionReceiptRepo.save(receiverReceipt);
        log.info(String.format("Receiver TransactionReceipt generated:: %s", createdReceiverReceipt.getId()));
        log.info(String.format("Done processing transaction:: %s", transactionEntity.getId()));

        return transactionEntity;
    }

    private void initializeTransaction(TransactionEntity transactionEntity) {
        System.out.println("transactionEntity: " + transactionEntity);
        transactionEntity.setTransactionDate(new Date());
        transactionEntity.setStatus(StatusType.PENDING);
        transactionRepo.save(transactionEntity);
        transactionEntity.setStatus(StatusType.INITIATED);
    }

    private void logTransactionAudit(TransactionEntity transactionEntity, String oldData, String newData, ActionType action) throws JsonProcessingException {
        TransactionAuditEntity transactionAuditEntity = TransactionAuditEntity.builder()
                .transaction(transactionEntity)
                .oldData(oldData)
                .newData(newData)
                .action(action)
                .performedAt(new Date())
                .performedBy(transactionEntity.getSender())
                .build();
        transactionAuditService.createTransactionAudit(transactionAuditEntity);
    }

    private void validateTransactionInputAndLogAudit(TransactionEntity transactionEntity) throws JsonProcessingException {
        logTransactionAudit(transactionEntity,
                objectMapper.writeValueAsString(Map.of("status", "INITIATED")),
                objectMapper.writeValueAsString(Map.of("status", "VALIDATING")),
                ActionType.VALIDATE);
        if (validateTransactionInput(transactionEntity)) {
            logTransactionAudit(transactionEntity,
                    objectMapper.writeValueAsString(Map.of("status", "VALIDATING")),
                    objectMapper.writeValueAsString(Map.of("status", "VALIDATED")),
                    ActionType.VALIDATE);
        }
    }

    private void authenticateTransactionAndLogAudit(TransactionEntity transactionEntity) throws JsonProcessingException {
        logTransactionAudit(transactionEntity,
                objectMapper.writeValueAsString(Map.of("status", "VALIDATED")),
                objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATING")),
                ActionType.AUTHENTICATE);
        if (validateTransactionPin(transactionEntity)) {
            logTransactionAudit(transactionEntity,
                    objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATING")),
                    objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATED")),
                    ActionType.AUTHENTICATE);
        }
    }

    private void preProcessTransactionAndLogAudit(TransactionEntity transactionEntity) throws JsonProcessingException {
        boolean isTransactionReadyForProcessing = isBalanceSufficient(transactionEntity.getSender(), transactionEntity.getAmount()) && usersActive(
                transactionEntity
        );
        logTransactionAudit(transactionEntity,
                objectMapper.writeValueAsString(Map.of("status", "AUTHENTICATED")),
                objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSING")),
                ActionType.PRE_PROCESS);
        if (isTransactionReadyForProcessing) {
            logTransactionAudit(transactionEntity,
                    objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSING")),
                    objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSED")),
                    ActionType.PRE_PROCESS);
        } else {
            transactionEntity.setStatus(StatusType.FAILED);
            transactionRepo.save(transactionEntity);
            throw new TransactionException.InvalidTransactionException("Transaction not ready for processing");
        }
    }

    private void processTransactionAndLogAudit(TransactionEntity transactionEntity) throws JsonProcessingException {
        logTransactionAudit(transactionEntity,
                objectMapper.writeValueAsString(Map.of("status", "PRE-PROCESSED")),
                objectMapper.writeValueAsString(Map.of("status", "PROCESSING")),
                ActionType.PROCESS);
        try {
            Map<String, Double> balances = processTransaction(transactionEntity);
            logTransactionAudit(transactionEntity,
                    objectMapper.writeValueAsString(Map.of("status", "PROCESSING")),
                    objectMapper.writeValueAsString(Map.of("status", "PROCESSED", "senderBalance", balances.get("senderBalance"), "receiverBalance", balances.get("receiverBalance"))),
                    ActionType.PROCESS);
        } catch (Exception e) {
            transactionEntity.setStatus(StatusType.FAILED);
            transactionRepo.save(transactionEntity);
            throw new TransactionException.BadTransaction("An error occurred during transaction");

        }
    }


    public List<TransactionEntity> getTransactions() {
        return StreamSupport.stream(transactionRepo.findAllByOrderByTransactionDateDesc().spliterator(), false).toList();
    }

    public TransactionEntity getTransaction(String id) {
        return transactionRepo.findById(id).orElse(null);
    }
}
