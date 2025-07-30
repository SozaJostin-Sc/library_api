package com.personal.library.library_api.util.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.personal.library.library_api.util.isbn.ISBN.Type;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {

    private Type type;

    @Override
    public void initialize(ISBN constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El ISBN no puede estar vacío")
                    .addConstraintViolation();
            return false;
        }

        // Elimina guiones y espacios
        String cleanedIsbn = isbn.replaceAll("[\\s-]+", "");

        try {
            return switch (type) {
                case ISBN10 -> validateISBN10(cleanedIsbn, context);
                case ISBN13 -> validateISBN13(cleanedIsbn, context);
                case BOTH -> validateISBN10(cleanedIsbn, context) ||
                        validateISBN13(cleanedIsbn, context);
                default -> throw new IllegalStateException("Tipo de ISBN no soportado: " + type);
            };
        } catch (ISBNValidationException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    private boolean validateISBN10(String isbn, ConstraintValidatorContext context) throws ISBNValidationException {
        if (isbn.length() != 10) {
            throw new ISBNValidationException("ISBN-10 debe tener exactamente 10 caracteres");
        }
        return isValidISBN10(isbn);
    }

    private boolean validateISBN13(String isbn, ConstraintValidatorContext context) throws ISBNValidationException {
        if (isbn.length() != 13) {
            throw new ISBNValidationException("ISBN-13 debe tener exactamente 13 caracteres");
        }
        return isValidISBN13(isbn);
    }

    private boolean isValidISBN10(String isbn) throws ISBNValidationException {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                throw new ISBNValidationException("Los primeros 9 caracteres del ISBN-10 deben ser dígitos");
            }
            int digit = Character.getNumericValue(c);
            sum += (digit * (10 - i));
        }

        char lastChar = isbn.charAt(9);
        if (!(Character.isDigit(lastChar) || (lastChar == 'X' || lastChar == 'x'))) {
            throw new ISBNValidationException("El último carácter del ISBN-10 debe ser un dígito o 'X'");
        }

        int lastDigit = (lastChar == 'X' || lastChar == 'x') ? 10 : Character.getNumericValue(lastChar);
        sum += lastDigit;

        if (sum % 11 != 0) {
            throw new ISBNValidationException("El checksum del ISBN-10 no es válido");
        }

        return true;
    }

    private boolean isValidISBN13(String isbn) throws ISBNValidationException {
        for (int i = 0; i < 13; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                throw new ISBNValidationException("Todos los caracteres del ISBN-13 deben ser dígitos");
            }
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = 10 - (sum % 10);
        if (checksum == 10) checksum = 0;

        int lastDigit = Character.getNumericValue(isbn.charAt(12));
        if (checksum != lastDigit) {
            throw new ISBNValidationException("El checksum del ISBN-13 no es válido");
        }

        return true;
    }

    // Excepción personalizada para validación de ISBN
    private static class ISBNValidationException extends Exception {
        public ISBNValidationException(String message) {
            super(message);
        }
    }
}