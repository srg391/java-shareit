package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto getRequest(long requestId, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос c id=" + requestId + " не существует!"));
        return itemRequestMapper.createDtoItemRequest(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestOfUser(long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        int page = from / size;
        List<ItemRequest> requests = itemRequestRepository.getItemRequestsByRequester(user.getId(), PageRequest.of(page, size));
        return itemRequestMapper.createDtoItemRequestList(requests);
    }

    @Override
    public List<ItemRequestDto> getAllRequestsOfUsers(long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        int page = from / size;
        List<ItemRequest> requests = itemRequestRepository.getItemRequestsByRequesters(user.getId(), PageRequest.of(page, size));
        return itemRequestMapper.createDtoItemRequestList(requests);
    }

    @Override
    public ItemRequestDto createRequest(long userId, NewestItemRequestDto newestItemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        LocalDateTime createdDateTime = LocalDateTime.now();
        ItemRequest itemRequest = itemRequestMapper.createItemRequest(newestItemRequestDto, createdDateTime, user);
        ItemRequest itemRequestFromRepository = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.createDtoItemRequest(itemRequestFromRepository);
    }
}
