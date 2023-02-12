package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemWithBookingDtoTest {
    @Autowired
    private JacksonTester<ItemWithBookingDto> jacksonTester;

    @Test
    void createDtoItemTest() throws Exception {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "вещь", "описание вещи", true, 1L, null, null, null);

        JsonContent<ru.practicum.shareit.item.dto.ItemWithBookingDto> result = jacksonTester.write(itemWithBookingDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemWithBookingDto.getAvailable());
    }
}
