package ru.practicum.shareit.item.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@JsonTest
class ItemMapperTest {

    @Autowired
    private JacksonTester<ItemWithBookingDto> jacksonTester;

    @Test
    void createDtoItemTest() {
    }

    @Test
    void createDtoListItemTest() {
    }

    @Test
    void createItemTest() {
        User user = new User(1L, "Sergey1", "sergey1@gmail.com");
        ItemDto itemDto = new ItemDto(1L, "вещь", "описание вещи", true, 1L);
        Item item = new Item(1L, "вещь", "описание вещи", true, user, 1L);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getRequestId(), itemDto.getRequestId());
    }

    @SneakyThrows
    @Test
    void createDtoItemWithBookingTest() {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, null, null, null);
        JsonContent<ItemWithBookingDto> result = jacksonTester.write(itemWithBookingDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemWithBookingDto.getAvailable());
    }
}