package ru.practicum.gateway.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.Create;
import ru.practicum.gateway.user.dto.UserRequestDto;

import javax.validation.Valid;

@Validated
@AllArgsConstructor
@Controller
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        ResponseEntity<Object> user = userClient.getUser(userId);
        log.debug("Пользователь с id :" + userId);
        return user;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        ResponseEntity<Object> users = userClient.getAllUsers();
        log.debug("Найти всех пользователей");
        return users;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @Validated({Create.class})
                                                 @RequestBody UserRequestDto userRequestDto) {
        ResponseEntity<Object> userCreated = userClient.createUser(userRequestDto);
        log.debug("Создан новый пользователь");
        return userCreated;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable("userId") long userId,
                                             @RequestBody UserRequestDto userRequestDto) {
        ResponseEntity<Object> userUpdated = userClient.updateUser(userId, userRequestDto);
        log.debug("Изменен пользователь");
        return userUpdated;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") long userId) {
        ResponseEntity<Object> userDeleted = userClient.deleteUser(userId);
        log.debug("Удален пользователь с id :" + userId);
        return userDeleted;
    }
}
