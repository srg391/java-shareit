package ru.practicum.shareit.booking;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Booking {

    long id;
    @NotNull(message = "Дата и время начала бронирования не соотвествует!")
    LocalDateTime start;
    @NotNull(message = "Дата и время окончания бронирования не соотвествует!")
    LocalDateTime end;
    @NotBlank(message = "Название не соотвествует!")
    String item;
    @NotBlank(message = "Имя бронирующего не соотвествует!")
    String booker;
    @NotBlank(message = "Статус брони не соотвествует!")
    String status;
}
