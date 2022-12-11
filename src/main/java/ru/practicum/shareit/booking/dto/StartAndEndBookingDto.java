package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.EndAfterStart;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "itemId")
@NoArgsConstructor
@AllArgsConstructor
@EndAfterStart
public class StartAndEndBookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
