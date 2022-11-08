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

    private Long id;
    @NotNull(message = "Дата и время начала бронирования не соотвествует!")
    private LocalDateTime start;
    @NotNull(message = "Дата и время окончания бронирования не соотвествует!")
    private LocalDateTime end;
    @NotBlank(message = "Название не соотвествует!")
    private String item;
    @NotBlank(message = "Имя бронирующего не соотвествует!")
    private String booker;
    @NotBlank(message = "Статус брони не соотвествует!")
    private String status;
}
