package ru.practicum.shareit.dto;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBookingDto {

    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;
    private List<CommentDto> comments;
    private Booking lastBooking;
    private Booking nextBooking;

    @Getter
    @Setter
    @EqualsAndHashCode(of = "id")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Booking {
        Long id;
        Long bookerId;
    }
}
