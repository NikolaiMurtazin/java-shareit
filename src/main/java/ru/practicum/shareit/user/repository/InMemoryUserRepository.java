package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(++lastId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        User userToUpdate = users.get(userId);
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        users.put(userToUpdate.getId(), userToUpdate);
        return userToUpdate;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email) && !user.getId().equals(id));
    }

}
