package ru.practicum.common.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LocalDateTimeValidator.class)
public @interface ValidStartDate {

    String message() default "Invalid event date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
