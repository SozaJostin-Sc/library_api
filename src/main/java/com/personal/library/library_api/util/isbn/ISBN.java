package com.personal.library.library_api.util.isbn;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNValidator.class) // Clase que implementa la lógica
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBN {

    String message() default "ISBN inválido"; // Mensaje de error
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Type type() default  Type.BOTH;

    enum Type{
        ISBN10,
        ISBN13,
        BOTH
    }
}