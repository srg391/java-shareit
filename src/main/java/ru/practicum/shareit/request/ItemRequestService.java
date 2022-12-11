package ru.practicum.shareit.request;

import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    public ItemRequestDto getRequest(long requestId, long userId);

    public List<ItemRequestDto> getRequestOfUser(long userId, int from, int size);

    public List<ItemRequestDto> getAllRequestsOfUsers(long userId, int from, int size);

    public ItemRequestDto createRequest(long userId, NewestItemRequestDto newestItemRequestDto) throws ItemNotAvailableException;
}
