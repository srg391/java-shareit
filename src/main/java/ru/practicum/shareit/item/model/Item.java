package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;
    @NotBlank(message = "Название не соотвествует!")
    private String name;
    @NotBlank(message = "Описание не соотвествует!")
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
