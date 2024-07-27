package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final int MAX_SIZE = 30;

    @Override
    public Collection<UserDto> getAll() {
        Collection<User> users = userRepository.findAll();
        return users.stream().map(UserMapper.INSTANCE::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("GET Пользователь с id={} не найден", userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto create(UserCreateDto userCreateDto) {
        User user = userRepository.save(UserMapper.INSTANCE.toUser(userCreateDto));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(long userId, UserUpdateDto userUpdateDto) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("UPDATE-USER Пользователь с id={} не найден", userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });

        if (userUpdateDto.getName() != null && !userUpdateDto.getName().isBlank()) {
            if (userUpdateDto.getName().length() > MAX_SIZE) {
                throw new IllegalArgumentException("Имя не должно превышать " + MAX_SIZE + " символов");
            }
            userToUpdate.setName(userUpdateDto.getName());
        }

        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank()) {
            if (userUpdateDto.getEmail().length() > MAX_SIZE) {
                throw new IllegalArgumentException("Email не должен превышать " + MAX_SIZE + " символов");
            }
            if (!userToUpdate.getEmail().equals(userUpdateDto.getEmail())) {
                userToUpdate.setEmail(userUpdateDto.getEmail());
            }
        }

        return UserMapper.INSTANCE.toUserDto(userRepository.save(userToUpdate));
    }

    @Override
    @Transactional
    public void delete(long userId) {
        checkUserExistence(userId, "DELETE-USER");
        userRepository.deleteById(userId);
    }

    private void checkUserExistence(Long userId, String method) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("{} Пользователь с id={} не найден", method, userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
    }
}
