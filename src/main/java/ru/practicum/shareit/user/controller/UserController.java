package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAll() {
        log.info("GET /users request");
        Collection<UserDto> users = userService.getAll();
        log.info("GET /users response: success {}", users.size());
        return users;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("GET /users/{} request", userId);
        UserDto userDto = userService.getById(userId);
        log.info("GET /users/{} response: success {}", userId, userDto);
        return userDto;
    }

    @PostMapping
    public UserDto create(@Validated @RequestBody UserDto userDto) {
        log.info("POST /users request: {}", userDto);
        UserDto createdUserDto = userService.create(userDto);
        log.info("POST /users response: success {}", createdUserDto);
        return createdUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                        @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} request: {}", userId, userDto);
        UserDto updateUserDto = userService.update(userId, userDto);
        log.info("PATCH /users/{} response: success {}", userId, updateUserDto);
        System.out.println("ошибка где-то здесь");
        return updateUserDto;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("DELETE /users/{} request", userId);
        userService.delete(userId);
        log.info("DELETE /users/{} response: success", userId);
    }
}
