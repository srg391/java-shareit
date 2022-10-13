package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto getUser(long userId);

    public List<UserDto> getAllUsers();

    public UserDto createUser(UserDto userDto);

    public UserDto updateUser(long userId, UserDto userDto);

    public void deleteByUserId(long userId);
}
