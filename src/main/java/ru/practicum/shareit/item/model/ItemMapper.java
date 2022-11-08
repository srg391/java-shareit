package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemMapper {

    public ItemDto createDtoItem(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()
        );
    }

    public List<ItemDto> createDtoListItem(List<Item> items) {
        return items.stream()
                .map(this::createDtoItem)
                .collect(Collectors.toList());
    }


    public static Item createItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                itemDto.getRequest()
        );
    }
}
