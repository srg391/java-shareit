package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        UserDto userDto = userService.getUser(userId);
        log.debug("Пользователь с id :" + userId);
        return userDto;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> usersList = userService.getAllUsers();
        log.debug("Количество пользователей :" + usersList.size());
        return usersList;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        UserDto userDtoCreated = userService.createUser(userDto);
        log.debug("Создан новый пользователь");
        return userDtoCreated;
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@PathVariable("userId") long userId, @RequestBody UserDto userDto) {
        UserDto userDtoUpdated = userService.updateUser(userId, userDto);
        log.debug("Изменен пользователь с id :" + userId);
        return userDtoUpdated;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteByUserId(userId);
        log.debug("Удален пользователь с id :" + userId);
    }
}
