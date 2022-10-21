package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private final ItemServiceImpl itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        ItemDto itemDto = itemService.getItem(itemId);
        log.debug("Вещь с id :" + itemId);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemDto> itemsList = itemService.getAllItemsOfUser(userId);
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
}
