package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void createDtoItemRequestTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "вещь", LocalDateTime.now(), null);
        JsonContent<ItemRequestDto> jsonResult = jacksonTester.write(itemRequestDto);

        assertThat(jsonResult).hasJsonPath("$.id");
        assertThat(jsonResult).hasJsonPath("$.description");
        assertThat(jsonResult).extractingJsonPathValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }
}
