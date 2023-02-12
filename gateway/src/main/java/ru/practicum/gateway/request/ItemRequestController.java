package ru.practicum.gateway.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.request.dto.ItemRequestGatewayDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final String httpHeaderUserId = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(httpHeaderUserId) long userId,
                                     @PathVariable("requestId") long requestId) {
        ResponseEntity<Object> itemRequest = requestClient.getRequest(requestId, userId);
        log.debug("Запрос с id :" + requestId);
        return itemRequest;
    }

    @GetMapping
    public ResponseEntity<Object> getRequestOfUser(@RequestHeader (httpHeaderUserId) long userId,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        ResponseEntity<Object> requests = requestClient.getRequestOfUser(userId, from, size);
        log.debug("Запрос пользователя с id = : " + userId);
        return requests;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOfUser(@RequestHeader (httpHeaderUserId) long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                     @Positive  @RequestParam(name = "size", defaultValue = "10") int size) {
        ResponseEntity<Object> requests = requestClient.getAllRequestsOfUser(userId, from, size);
        log.debug("Все Запросы пользователя с id = : " + userId);
        return requests;
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader (httpHeaderUserId) long userId,
                                        @Validated({Create.class}) @RequestBody ItemRequestGatewayDto itemRequestGatewayDto) {
        ResponseEntity<Object> itemRequest = requestClient.createRequest(userId, itemRequestGatewayDto);
        log.debug("Создан запрос у пользователя с id :" + userId);
        return itemRequest;
    }
}
