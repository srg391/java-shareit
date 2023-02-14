package ru.practicum.gateway.item.dto;

import lombok.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequestDto {
    @NotNull(groups = {Update.class})
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;
}
