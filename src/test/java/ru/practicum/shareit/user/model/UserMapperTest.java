package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class UserMapperTest {

    UserMapper userMapper;

    private User user1;

    private User user2;


    @BeforeEach
    void beforeEach() {
        userMapper = new UserMapper();
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        user2 = new User(2L, "Valery2", "valery2@mail.ru");
    }

    @Test
    void createUserTest() {
        UserDto userDto1 = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        UserDto userDto2 = new UserDto(2L, "Valery2", "valery2@mail.ru");

        User userSergey = userMapper.createUser(userDto1);
        User userValery = userMapper.createUser(userDto2);

        assertEquals(userSergey, user1);
        assertEquals(userValery, user2);
    }

    @Test
    void createDtoUserTest() {
        UserDto userDtoSergey = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        UserDto userDtoValery = new UserDto(2L, "Valery2", "valery2@mail.ru");

        UserDto userDtoSergey1 = userMapper.createDtoUser(user1);
        UserDto userDtoValery2 = userMapper.createDtoUser(user2);

        assertEquals(userDtoSergey1, userDtoSergey);
        assertEquals(userDtoValery2, userDtoValery);
    }

    @Test
    void createDtoListUserTest() {
        UserDto userDto1 = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        UserDto userDto2 = new UserDto(2L, "Valery2", "valery2@mail.ru");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<UserDto> usersDtoNew = new ArrayList<>();
        usersDtoNew.add(userDto1);
        usersDtoNew.add(userDto2);

        List<UserDto> usersDto = userMapper.createDtoListUser(users);

        assertEquals(usersDto, usersDtoNew);
    }
}