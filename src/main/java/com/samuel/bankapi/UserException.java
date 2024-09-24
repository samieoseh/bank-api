package com.samuel.bankapi;

public class UserException {
    // this class should hold all exceptions related to the User entity

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }


    public static class UserNotAuthenticatedException extends RuntimeException {
        public UserNotAuthenticatedException(String message) {
            super(message);
        }
    }

    public static class UserNotDeletedException extends RuntimeException {
        public UserNotDeletedException(String message) {
            super(message);
        }
    }

    public static class UserNotUpdatedException extends RuntimeException {
        public UserNotUpdatedException(String message) {
            super(message);
        }
    }

    public static class UserNotCreatedException extends RuntimeException {
        public UserNotCreatedException(String message) {
            super(message);
        }
    }

    public static class UserNotLoggedInException extends RuntimeException {
        public UserNotLoggedInException(String message) {
            super(message);
        }
    }

    public static class UserNotRegisteredException extends RuntimeException {
        public UserNotRegisteredException(String message) {
            super(message);
        }
    }

    public static class UserNotAuthorizedException extends RuntimeException {
        public UserNotAuthorizedException(String message) {
            super(message);
        }
    }



}
