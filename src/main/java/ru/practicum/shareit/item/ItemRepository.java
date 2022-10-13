package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    public Optional<Item> getByItemId(long itemId);

    public List<Item> getAllItems();

    public Item saveItem(Item item);

    public Item updateItem(Item item);

    public void deleteByItemId(long itemId);
}
