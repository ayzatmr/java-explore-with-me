package ru.practicum.common.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ValidStartDate, LocalDateTime> {

    String message;

    @Override
    public void initialize(ValidStartDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return localDateTime.minusHours(2).isAfter(LocalDateTime.now());
    }
}
