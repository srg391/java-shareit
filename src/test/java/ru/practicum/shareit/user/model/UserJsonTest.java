package ru.practicum.shareit.user.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @SneakyThrows
    @Test
    void createDtoUserTest() {
        UserDto userDtoSergey = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        UserDto userDtoValery = new UserDto(2L, "Valery2", "valery2@mail.ru");

        JsonContent<UserDto> result = jacksonTester.write(userDtoSergey);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.email");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDtoSergey.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoSergey.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoSergey.getEmail());

        JsonContent<UserDto> result1 = jacksonTester.write(userDtoValery);
        assertThat(result1).hasJsonPath("$.id");
        assertThat(result1).hasJsonPath("$.name");
        assertThat(result1).hasJsonPath("$.email");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDtoSergey.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoSergey.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoSergey.getEmail());
    }
}
