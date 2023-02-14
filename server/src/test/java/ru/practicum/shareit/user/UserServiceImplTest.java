package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    private UserService userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user1;

    private User user2;

    @BeforeEach
    void beforeEach() {
        userServiceImpl = new UserServiceImpl(userRepository, userMapper);
        user1 = new User(1L, "Sergey1", "sergey1@gmail.com");
        user2 = new User(2L, "Valery2", "valery2@mail.ru");
    }

    @Test
    void createUserTest() {
        UserDto userDto1 = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        when(userRepository.save(user1))
                .thenReturn(user1);
        userServiceImpl.createUser(userDto1);
        verify(userRepository, times(1)).save(user1);

        UserDto userDto2 = new UserDto(2L, "Valery2", "valery2@mail.ru");
        when(userRepository.save(user2))
                .thenReturn(user2);
        userServiceImpl.createUser(userDto2);
        verify(userRepository, times(1)).save(user2);
    }

    @Test
    void getUserTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));
        userServiceImpl.getUser(1L);
        verify(userRepository, times(1)).findById(1L);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user2));
        userServiceImpl.getUser(2L);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testGetUserByWrongId() {
        assertThrows(NotFoundException.class, () -> userServiceImpl.getUser(330L));
    }

    @Test
    void testDeleteWrongId() {
        assertThrows(NotFoundException.class, () -> userServiceImpl.deleteByUserId(330L));
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto1 = new UserDto(1L, "Sergey1", "sergey1@gmail.com");
        userServiceImpl.createUser(userDto1);
        UserDto userDto2 = new UserDto(2L, "Valery2", "valery2@mail.ru");
        userServiceImpl.createUser(userDto2);
        final List<UserDto> usersDto = userServiceImpl.getAllUsers();
        final List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        when(userRepository.findAll())
                .thenReturn(allUsers);

        assertNotNull(usersDto);
        assertEquals(0, usersDto.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateAndDeleteUserByIdTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));
        UserDto userDto1 = new UserDto(1L, "Sergey1 update", "sergey1@gmail.com");
        userServiceImpl.updateUser(1L, userDto1);
        userServiceImpl.deleteByUserId(1L);
        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void testUpdateUserByWrongId() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        UserDto userDto1 = new UserDto(1L, "Sergey1 update", "sergey1@gmail.com");
        assertThrows(NotFoundException.class, () -> userServiceImpl.updateUser(330L, userDto1));
    }

    @Test
    void getUserWithExceptionTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> userServiceImpl.getUser(1L));
        verify(userRepository, times(1)).findById(1L);
        assertEquals("Пользователь c id=" + 1L + " не существует!", exception.getMessage());
    }
}
