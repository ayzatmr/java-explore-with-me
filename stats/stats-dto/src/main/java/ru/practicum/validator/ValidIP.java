package ru.practicum.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = IPValidator.class)
public @interface ValidIP {

    String message() default "Wrong IP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
