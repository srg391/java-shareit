package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "itemId")
@NoArgsConstructor
@AllArgsConstructor
public class StartAndEndBookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
