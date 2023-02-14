package ru.practicum.gateway.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.Update;
import ru.practicum.gateway.item.dto.CommentRequestDto;
import ru.practicum.gateway.item.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final String httpHeaderUserId = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(httpHeaderUserId) long userId, @PathVariable Long itemId) {
        ResponseEntity<Object> itemWithBooking = itemClient.getItem(userId, itemId);
        log.debug("Вещь с id :" + itemId);
        return itemWithBooking;
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader(httpHeaderUserId) long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        ResponseEntity<Object> items = itemClient.getAllItemsOfUser(userId, from, size);
        log.debug("Вещи пользователя с id = :" + userId);
        return items;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@RequestParam("text") String text,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        ResponseEntity<Object> items = itemClient.getItemsBySearch(text, from, size);
        log.debug("Вещи согласно параметров поиска :" + text);
        return items;
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(httpHeaderUserId) long userId,
                                             @Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto) {
        ResponseEntity<Object> itemCreated = itemClient.createItem(userId, itemRequestDto);
        log.debug("Создана вещь у пользователя с id :" + userId);
        return itemCreated;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(httpHeaderUserId) long userId, @Validated({Update.class})
    @PathVariable long itemId, @RequestBody ItemRequestDto itemRequestDto) {
        ResponseEntity<Object> itemUpdated = itemClient.updateItem(userId, itemId, itemRequestDto);
        log.debug("Изменена вещь c id :" +  itemId + "у пользователя с id :" + userId);
        return itemUpdated;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(httpHeaderUserId) long userId, @PathVariable long itemId,
                                             @Validated({Create.class}) @RequestBody CommentRequestDto commentRequestDto) {
        ResponseEntity<Object> commentSaved = itemClient.addComment(userId, itemId, commentRequestDto);
        log.debug("Добавлен комментарий к вещи с id :" + itemId);
        return commentSaved;
    }
}
