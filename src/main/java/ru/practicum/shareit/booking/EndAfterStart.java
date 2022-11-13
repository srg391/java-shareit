package ru.practicum.shareit.booking;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EndAfterStartValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndAfterStart {

    String message() default  "Дата окончания бронирования должна быть позже, чем дата старта бронирования!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
