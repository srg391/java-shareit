package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new LinkedHashMap<>();
    private long nextId = 0;

    private long getUserId() {
        return ++nextId;
    }

    @Override
    public Optional<User> getByUserId(long userId) {
        Optional<User> userOptional = Optional.ofNullable(users.get(userId));
        return userOptional;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        return userList;
    }

    @Override
    public User saveUser(User user) {
        user.setId(getUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(),user);
        return user;
    }


    @Override
    public void deleteByUserId(long userId) {
        users.remove(userId);
    }
}
