package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.*;

public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new LinkedHashMap<>();

    private long nextId = 0;

    private long getItemId() {
        return ++nextId;
    }

    @Override
    public Optional<Item> getByItemId(long itemId) {
        Optional<Item> itemOptional = Optional.ofNullable(items.get(itemId));
        return itemOptional;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> itemsList = new ArrayList<>(items.values());
        return itemsList;
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(getItemId());
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(),item);
        return item;
    }


    @Override
    public void deleteByItemId(long itemId) {
        items.remove(itemId);
    }

}
