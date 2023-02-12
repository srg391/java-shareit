package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final String httpHeaderUserId = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getItem(@RequestHeader(httpHeaderUserId) long userId, @PathVariable Long itemId) {
        ItemWithBookingDto itemWithBookingDto = itemService.getItem(userId, itemId);
        log.debug("Вещь с id :" + itemId);
        return itemWithBookingDto;
    }

    @GetMapping
    public List<ItemWithBookingDto> getAllItemsOfUser(@RequestHeader(httpHeaderUserId) long userId,
                                                      @RequestParam(name = "from", defaultValue = "0") int from,
                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemWithBookingDto> itemsList = itemService.getAllItemsOfUser(userId, from, size);
        log.debug("Количество вещей :" + itemsList.size());
        return itemsList;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam("text") String text,
                                          @RequestParam(name = "from", defaultValue = "0") int from,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        List<ItemDto> itemsList = itemService.getItemsBySearch(text, from, size);
        log.debug("Количество вещей согласно параметров поиска :" + itemsList.size());
        return itemsList;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(httpHeaderUserId) long userId, @RequestBody ItemDto itemDto) {
        ItemDto itemDtoCreated = itemService.createItem(userId,itemDto);
        log.debug("Создана вещь у пользователя с id :" + userId);
        return itemDtoCreated;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(httpHeaderUserId) long userId,
                              @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        ItemDto itemDtoUpdated = itemService.updateItem(userId, itemDto);
        log.debug("Создана вещь c id :" +  itemId + "у пользователя с id :" + userId);
        return itemDtoUpdated;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestHeader(httpHeaderUserId) long userId, @PathVariable long itemId, @RequestBody CommentDto commentDto) {
        commentDto.setItemId(itemId);
        CommentDto commentDtoSaved = commentService.createComment(commentDto, userId);
        log.debug("Добавлен комментарий к вещи с id :" + itemId);
        return commentDtoSaved;
    }
}
