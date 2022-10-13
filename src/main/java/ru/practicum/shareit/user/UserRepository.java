package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<User> getByUserId(long userId);

    public List<User> getAllUsers();

    public User saveUser(User user);

    public User updateUser(User user);

    public void deleteByUserId(long userId);
}
