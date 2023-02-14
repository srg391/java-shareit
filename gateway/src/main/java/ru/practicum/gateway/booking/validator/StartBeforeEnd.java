package ru.practicum.gateway.booking.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default  "Дата начала бронирования должна быть раньше даты окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
