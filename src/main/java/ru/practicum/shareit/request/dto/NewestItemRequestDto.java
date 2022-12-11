package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewestItemRequestDto {

    @NotBlank(groups = {Create.class})
    private String description;
}
