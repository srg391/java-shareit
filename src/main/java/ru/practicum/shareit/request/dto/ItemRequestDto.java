package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<Item> items;

    @Getter
    @Setter
    @EqualsAndHashCode(of = "id")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private Long id;

        private String name;

        private String description;

        private Boolean available;

        private Long requestId;
    }
}
