package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemService;
    private final CommentService commentService;

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        ItemWithBookingDto itemWithBookingDto = null;
        try {
            itemWithBookingDto = itemService.getItem(userId, itemId);
        } catch (Exception e) {
            System.out.printf("Ошибка" + e.getStackTrace());
        }
        log.debug("Вещь с id :" + itemId);
        return itemWithBookingDto;
    }

    @GetMapping
    public List<ItemWithBookingDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemWithBookingDto> itemsList = itemService.getAllItemsOfUser(userId);
        log.debug("Количество вещей :" + itemsList.size());
        return itemsList;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam("text") String text) {
        List<ItemDto> itemsList = itemService.getItemsBySearch(text);
        log.debug("Количество вещей согласно параметров поиска :" + itemsList.size());
        return itemsList;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        ItemDto itemDtoCreated = itemService.createItem(userId,itemDto);
        log.debug("Создана вещь у пользователя с id :" + userId);
        return itemDtoCreated;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Update.class})
    @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        ItemDto itemDtoUpdated = itemService.updateItem(userId, itemDto);
        log.debug("Создана вещь c id :" +  itemId + "у пользователя с id :" + userId);
        return itemDtoUpdated;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        commentDto.setItemId(itemId);
        CommentDto commentDtoSaved = commentService.createComment(commentDto, userId);
        log.debug("Добавлен комментарий к вещи с id :" + itemId);
        return commentDtoSaved;
    }
}
