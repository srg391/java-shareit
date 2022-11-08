package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getByUserId(long userId);

    List<User> getAllUsers();

    User saveUser(User user);

    User updateUser(User user);

    void deleteByUserId(long userId);
}
