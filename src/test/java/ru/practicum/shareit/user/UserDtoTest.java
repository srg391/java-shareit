package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void createDtoUserTest() throws Exception {
        UserDto userDto = new UserDto(1L, "Sergey1", "sergey1@gmail.com");

        JsonContent<UserDto> jsonResult = jacksonTester.write(userDto);
        assertThat(jsonResult).hasJsonPath("$.id");
        assertThat(jsonResult).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
    }
}
