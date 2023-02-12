package ru.practicum.gateway.request.dto;

import lombok.*;
import ru.practicum.gateway.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemRequestGatewayDto {
    @NotBlank(groups = {Create.class})
    private String description;
}
