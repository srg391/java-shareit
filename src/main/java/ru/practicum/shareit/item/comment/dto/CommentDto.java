package ru.practicum.shareit.item.comment.dto;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(groups = {Create.class})
    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime dateOfCreation;
}
