package ru.practicum.shareit.itemRequest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewestItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestMapperTest {

    private ItemRequestMapper itemRequestMapper;

    private User user;
    private ItemRequest itemRequest1;

    private ItemRequest itemRequest2;

    @BeforeEach
    void beforeEach() {
        itemRequestMapper = new ItemRequestMapper();
        user = new User(1L, "Sergey1", "sergey1@gmail.com");
        itemRequest1 = new ItemRequest(1L, "вещь", user, LocalDateTime.of(2021, 11, 3, 9, 55));
        itemRequest2 = new ItemRequest(2L, "другая вещь", user, LocalDateTime.of(2021, 11, 3, 10, 55));
    }

    @Test
    void createItemRequestTest() {
        List items = new ArrayList<>();
        NewestItemRequestDto itemRequestDto1 = new NewestItemRequestDto("вещь");
        NewestItemRequestDto itemRequestDto2 = new NewestItemRequestDto("другая вещь");

        ItemRequest itemRequestNew1 = itemRequestMapper.createItemRequest(itemRequestDto1, LocalDateTime.of(2021, 11, 3, 9, 55), user);
        ItemRequest itemRequestNew2 = itemRequestMapper.createItemRequest(itemRequestDto2, LocalDateTime.of(2021, 11, 3, 10, 55), user);

        assertEquals(itemRequestNew1.getDescription(), itemRequest1.getDescription());
        assertEquals(itemRequestNew2.getDescription(), itemRequest2.getDescription());
    }

    @Test
    void createDtoItemRequestTest() {
        List items = new ArrayList<>();
        User user1 = new User(2L, "Valery2", "valery2@mail.ru");
        Item item1 = new Item(1L, "вещь", "описание вещи", true, user1, 1L);
        Item item2 = new Item(2L, "другая вещь", "описание другой вещи", true, user1, 2L);
        items.add(item1);
        items.add(item2);

        NewestItemRequestDto itemRequestDto1 = new NewestItemRequestDto("вещь");
        NewestItemRequestDto itemRequestDto2 = new NewestItemRequestDto("другая вещь");

        ItemRequestDto itemRequestDtoNew1 = itemRequestMapper.createDtoItemRequest(itemRequest1);
        ItemRequestDto itemRequestDtoNew2 = itemRequestMapper.createDtoItemRequest(itemRequest2);

        assertEquals(itemRequestDtoNew1.getDescription(), itemRequestDto1.getDescription());
        assertEquals(itemRequestDtoNew2.getDescription(), itemRequestDto2.getDescription());

        ItemRequestDto itemRequestDtoThing1 = new ItemRequestDto(1L, "вещь", LocalDateTime.of(2021, 11, 3, 9, 55), items);

        assertEquals(itemRequestDtoThing1.getItems(), items);
    }

    @Test
    void createDtoListItemRequestTest() {
        ItemRequestDto itemRequestDtoNew1 = itemRequestMapper.createDtoItemRequest(itemRequest1);
        ItemRequestDto itemRequestDtoNew2 = itemRequestMapper.createDtoItemRequest(itemRequest2);

        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest1);
        itemRequests.add(itemRequest2);



        List<ItemRequestDto> itemRequestsDtoNew = new ArrayList<>();
        itemRequestsDtoNew.add(itemRequestDtoNew1);
        itemRequestsDtoNew.add(itemRequestDtoNew2);

        List<ItemRequestDto> itemRequestDto = itemRequestMapper.createDtoItemRequestList(itemRequests);

        assertEquals(itemRequestDto, itemRequestsDtoNew);
    }
}
