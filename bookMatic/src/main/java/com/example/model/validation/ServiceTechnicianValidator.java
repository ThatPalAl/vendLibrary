package com.example.model.validation;

import com.example.model.ServiceTechnician;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ServiceTechnicianValidator implements ConstraintValidator<ValidServiceTechnician, ServiceTechnician> {


    @Override
    public void initialize(ValidServiceTechnician constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ServiceTechnician technician, ConstraintValidatorContext context) {
        if (technician.isBeginner() && technician.getCertificate() != null) {
            return false;
        }
        if (technician.isExpert() && (technician.getCertificate() == null || technician.getCertificate().isEmpty())) {
            return false;
        }

        return true;
    }
}
