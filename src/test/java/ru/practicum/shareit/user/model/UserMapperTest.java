package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@JsonTest
class UserMapperTest {
    UserService userServiceImpl;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private User user1;

    private User user2;

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @BeforeEach
    void beforeEach() {
        userServiceImpl = new UserServiceImpl(userRepository, userMapper);
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        user2 = new User(2L, "Valery2", "valery2@mail.ru");
    }

    @Test
    void createDtoListUserTest() {
        UserDto userDto1 = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        UserDto userDto2 = new UserDto(2L, "Valery2", "valery2@mail.ru");

        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        user2 = new User(2L, "Valery2", "valery2@mail.ru");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        userServiceImpl.createUser(userDto1);
        userServiceImpl.createUser(userDto2);
        List<UserDto> usersDto = userMapper.createDtoListUser(users);
        List<UserDto> usersDtoNew = userServiceImpl.getAllUsers();
        assertEquals(usersDtoNew, usersDto);
    }


}