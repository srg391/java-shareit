package ru.practicum.shareit.itemRequest.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@JsonTest
class ItemRequestMapperTest {

    ItemRequestService itemRequestServiceImpl;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private Item item1;

    private Item item2;
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @SneakyThrows
    @Test
    void createDtoItemRequestTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "вещь", LocalDateTime.now(), null);
        JsonContent<ItemRequestDto> jsonResult = jacksonTester.write(itemRequestDto);

        assertThat(jsonResult).hasJsonPath("$.id");
        assertThat(jsonResult).hasJsonPath("$.description");
        assertThat(jsonResult).extractingJsonPathValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }

    @Test
    void createItemRequestTest() {
        User user = new User(1L, "Sergey1", "sergey1@gmail.com");
        Item item = new Item();
        item.setId(1L);
        item.setName("вещь");
        item.setDescription("описание вещи");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("вещь");
        itemRequestDto.setCreated(LocalDateTime.of(2021, 11, 3, 9, 55));
        itemRequestDto.setItems(new ArrayList<>());
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("вещь");
        itemRequest.setRequester(user);
        itemRequest.setDateOfCreation(LocalDateTime.of(2021, 11, 3, 9, 55));
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getItems(), itemRequestDto.getItems());
    }

}