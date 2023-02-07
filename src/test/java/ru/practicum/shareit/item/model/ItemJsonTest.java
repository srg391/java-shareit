package ru.practicum.shareit.item.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;



@JsonTest
class ItemJsonTest {

    @Autowired
    private JacksonTester<ItemWithBookingDto> jacksonTester;

    @Test
    void createItemTest() {
        User user = new User(1L, "Sergey1", "sergey1@gmail.com");
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("вещь");
        itemDto.setDescription("описание вещи");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setDescription("описание вещи");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(1L);
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