package ru.practicum.shareit.request.model;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestMapper {

    public ItemRequestDto createDtoItemRequest(@NonNull ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getDateOfCreation(),
                itemRequest.getItems().stream()
                        .map(i -> new ItemRequestDto.Item(i.getId(), i.getName(), i.getDescription(), i.getAvailable(),
                                itemRequest.getRequester().getId()))
                        .collect(Collectors.toList())
        );
    }

    public List<ItemRequestDto> createDtoItemRequestList(List<ItemRequest> requests) {
        return requests.stream()
                .map(this::createDtoItemRequest)
                .collect(Collectors.toList());
    }

    public ItemRequest createItemRequest(NewestItemRequestDto newestItemRequestDto, LocalDateTime createdDateTime, User user) {
        return new ItemRequest(
                null,
                newestItemRequestDto.getDescription(),
                user,
                createdDateTime
        );
    }
}
