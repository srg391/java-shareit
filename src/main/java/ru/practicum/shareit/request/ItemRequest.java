package ru.practicum.shareit.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequest {

    long id;
    @NotBlank(message = "Описание не соотвествует!")
    String description;
    @NotBlank(message = "Имя запросившего вещь не соотвествует!")
    String requester;
    @NotNull(message = "Дата и время бронирования не соотвествует!")
    LocalDateTime created;
}
