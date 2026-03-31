package com.example.cognitivetwin.common.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(
        validatedBy = {StrongPasswordValidator.class}
)
public @interface StrongPassword {

    String message() default "Password must be at least 8 characters long and contain an uppercase letter, lowercase letter, number, and special character";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
