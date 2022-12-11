package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto getUser(long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        return userMapper.createDtoUser(user);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.createDtoListUser(users);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User savedUser = validateUser(userDto);
        return userMapper.createDtoUser(savedUser);
    }

    private User validateUser(UserDto userDto) {
        User savedUser = null;
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().contains(" ") && userDto.getEmail().matches(".+@.+\\.[a-z]+")) {
                User user = userMapper.createUser(userDto);
                savedUser = userRepository.save(user);
            } else {
                throw new BadRequestException("Email не соотвествует!");
            }
        }
        return savedUser;
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        userRepository.save(user);
        return userMapper.createDtoUser(user);
    }

    @Override
    @Transactional
    public void deleteByUserId(long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        userRepository.delete(user);
    }

}
