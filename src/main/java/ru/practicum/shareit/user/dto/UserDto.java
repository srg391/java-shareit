package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    @Email
    private String email;

}
