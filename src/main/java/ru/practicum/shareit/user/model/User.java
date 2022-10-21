package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private Long id;
    @NotBlank(message = "Имя не соотвествует!")
    private String name;
    @NotBlank(message = "Адрес электронной почты не соотвествует!")
    @Email(message = "Адрес электронной почты не соотвествует!")
    private String email;

}
