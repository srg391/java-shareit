package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceImplTest {

    ItemService itemServiceImpl;

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemMapper itemMapper;

    private Item item;
    private User user;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        itemServiceImpl = new ItemServiceImpl(itemRepository, userRepository, itemMapper, bookingRepository);
        user = new User(1L, "Sergey1", "sergey1@gmail.com");
        item = new Item(1L, "вещь", "описание вещи", true, user, 1L);
        itemDto = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
    }

    @Test
    void createItemTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.save(any()))
                .thenReturn(item);
        itemServiceImpl.createItem(user.getId(), itemDto);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateItemTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRepository.save(any()))
                .thenReturn(item);
        itemServiceImpl.updateItem(user.getId(), itemDto);
        verify(itemRepository, times(1)).save(item);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void getItemTest() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        itemServiceImpl.getItem(item.getId(), user.getId());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void itemWithNotFoundTest() {
        when(itemRepository.findById(1L));
        final RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> itemServiceImpl.getItem(item.getId(), user.getId()));
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void getAllItemsOfUserTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        itemServiceImpl.createItem(user.getId(), itemDto);
        final PageImpl<Item> itemPage = new PageImpl<>(Collections.singletonList(item));
        when(itemRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(itemPage);

        final List<ItemWithBookingDto> itemDtos = itemServiceImpl.getAllItemsOfUser(1L, 0, 10);

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.size());

        verify(itemRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getItemsBySearchTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        itemServiceImpl.createItem(user.getId(), itemDto);
        final String text = "вещь";
        final PageImpl<Item> itemPage = new PageImpl<>(Collections.singletonList(item));
        when(itemRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(itemPage);

        itemServiceImpl.getItemsBySearch(text, 0, 10);

        verify(itemRepository, times(1)).findAll(PageRequest.of(0, 10));
    }
}
