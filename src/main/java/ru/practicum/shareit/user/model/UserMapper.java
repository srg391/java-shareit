package ru.practicum.shareit.user.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public UserDto createDtoUser(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public List<UserDto> createDtoListUser(List<User> users) {
        return users.stream()
                .map(this::createDtoUser)
                .collect(Collectors.toList());
    }


    public static User createUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
