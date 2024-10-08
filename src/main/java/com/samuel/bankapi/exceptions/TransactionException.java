package com.samuel.bankapi.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TransactionException {

    public static class BadTransaction extends RuntimeException {
        public BadTransaction(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionException extends RuntimeException {
        public InvalidTransactionException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentials extends  RuntimeException {
        public InvalidCredentials(String message) {
            super(message);
        }
    }
}
