package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> getByItemId(long itemId);

    List<Item> getAllItems();

    Item saveItem(Item item);

    Item updateItem(Item item);

    void deleteByItemId(long itemId);
}
