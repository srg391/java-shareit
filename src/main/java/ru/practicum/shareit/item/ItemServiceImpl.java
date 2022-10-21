package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepositoryImpl;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepositoryImpl itemRepository;
    @Autowired
    private final UserRepositoryImpl userRepository;
    @Autowired
    private final ItemMapper itemMapper;

    @Override
    public ItemDto getItem(long itemId) {
        final Item item = itemRepository.getByItemId(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemId + " не существует!"));
        return itemMapper.createDtoItem(item);
    }

    @Override
    public List<ItemDto> getAllItemsOfUser(long userId) {
        User owner = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        List<Item> items = itemRepository.getAllItems().stream()
                .filter(i -> i.getOwner() == owner)
                .collect(Collectors.toList());
        return itemMapper.createDtoListItem(items);
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.getAllItems().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
        return itemMapper.createDtoListItem(items);
    }

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        User owner = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        Item item = itemMapper.createItem(itemDto, owner);
        itemRepository.saveItem(item);
        return itemMapper.createDtoItem(item);
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        final Item item = itemRepository.getByItemId(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь c id=" + itemDto.getId() + " не существует!"));
        final User owner = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Хозяин c id=" + userId + " не существует!"));
        if ((item.getOwner().getId()).equals(owner.getId())) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            itemRepository.updateItem(item);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return itemMapper.createDtoItem(item);
    }
}
