package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;


public interface ItemService {

    public ItemWithBookingDto getItem(long userId, long itemId);

    public List<ItemWithBookingDto> getAllItemsOfUser(long userId);

    public List<ItemDto> getItemsBySearch(String text);

    public ItemDto createItem(long userId, ItemDto itemDto);

    public ItemDto updateItem(long userId, ItemDto itemDto);

}
