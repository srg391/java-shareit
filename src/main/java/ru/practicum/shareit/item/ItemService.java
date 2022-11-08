package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {

    public ItemDto getItem(long itemId);

    public List<ItemDto> getAllItemsOfUser(long userId);

    public List<ItemDto> getItemsBySearch(String text);

    public ItemDto createItem(long userId, ItemDto itemDto);

    public ItemDto updateItem(long userId, ItemDto itemDto);

}
