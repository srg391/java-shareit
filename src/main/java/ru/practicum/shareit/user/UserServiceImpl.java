package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepositoryImpl userRepository;
    @Autowired
    private final UniqueEmailsRepository uniqueEmailsRepository;
    @Autowired
    private final UserMapper userMapper;

    @Override
    public UserDto getUser(long userId) {
        final User user = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        return userMapper.createDtoUser(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        return userMapper.createDtoListUser(users);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        uniqueEmailsRepository.checkEmailForUniquenessAndValidity(userDto.getEmail());
        User user = userMapper.createUser(userDto);
        User savedUser = userRepository.saveUser(user);
        return userMapper.createDtoUser(savedUser);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        final User user = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            uniqueEmailsRepository.checkEmailForUniquenessAndValidity(userDto.getEmail());
            uniqueEmailsRepository.deleteEmailFromSetStorage(user.getEmail());
            user.setEmail(userDto.getEmail());
            userRepository.updateUser(user);
        }
        return userMapper.createDtoUser(user);
    }

    @Override
    public void deleteByUserId(long userId) {
        final User user = userRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        userRepository.deleteByUserId(userId);
        uniqueEmailsRepository.deleteEmailFromSetStorage(user.getEmail());
    }

}
