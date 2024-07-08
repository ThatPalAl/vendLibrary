package com.example.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceTechnicianValidator.class)
public @interface ValidServiceTechnician {
    String message() default "Invalid Service Technician data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
