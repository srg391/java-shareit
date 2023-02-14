package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final String httpHeaderUserId = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(httpHeaderUserId) long userId,
                                     @PathVariable("requestId") long requestId) {
        ItemRequestDto itemRequestDto = itemRequestService.getRequest(requestId, userId);
        log.debug("Запрос с id :" + requestId);
        return itemRequestDto;
    }

    @GetMapping
    public List<ItemRequestDto> getRequestOfUser(@RequestHeader (httpHeaderUserId) long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> requests = itemRequestService.getRequestOfUser(userId, from, size);
        log.debug("Количество запросов : " + requests.size());
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsOfUser(@RequestHeader (httpHeaderUserId) long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> requests = itemRequestService.getAllRequestsOfUsers(userId, from, size);
        log.debug("Количество запросов : " + requests.size());
        return requests;
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader (httpHeaderUserId) long userId,
                                        @RequestBody NewestItemRequestDto newestItemRequestDto) {
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(userId, newestItemRequestDto);
        log.debug("Создан запрос у пользователя с id :" + userId);
        return itemRequestDto;
    }

}
