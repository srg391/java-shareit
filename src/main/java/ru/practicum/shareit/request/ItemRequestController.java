package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable("requestId") long requestId) {
        ItemRequestDto itemRequestDto = itemRequestService.getRequest(requestId, userId);
        log.debug("Запрос с id :" + requestId);
        return itemRequestDto;
    }

    @GetMapping
    public List<ItemRequestDto> getRequestOfUser(@RequestHeader ("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> requests = itemRequestService.getRequestOfUser(userId, from, size);
        log.debug("Количество запросов : " + requests.size());
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsOfUser(@RequestHeader ("X-Sharer-User-Id") long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @Positive  @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemRequestDto> requests = itemRequestService.getAllRequestsOfUsers(userId, from, size);
        log.debug("Количество запросов : " + requests.size());
        return requests;
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader ("X-Sharer-User-Id") long userId,
                                       @Validated({Create.class}) @RequestBody NewestItemRequestDto newestItemRequestDto) {
        ItemRequestDto itemRequestDto = itemRequestService.createRequest(userId, newestItemRequestDto);
        log.debug("Создан запрос у пользователя с id :" + userId);
        return itemRequestDto;
    }

}
