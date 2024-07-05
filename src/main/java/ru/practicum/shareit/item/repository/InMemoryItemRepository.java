package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public Collection<Item> getAllByUsersId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item add(Long userId, Item item) {
        item.setOwnerId(userId);
        item.setId(++lastId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        Item updatedItem = items.get(itemId);
        if (userId.equals(updatedItem.getOwnerId())) {
            if (item.getName() != null) {
                updatedItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                updatedItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                updatedItem.setAvailable(item.getAvailable());
            }
        } else {
            throw new NotFoundException("The user's ID is different from the owner's ID");
        }
        items.put(updatedItem.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public void delete(Long userId, Long itemId) {
        if (userId.equals(items.get(itemId).getOwnerId())) {
            items.remove(itemId);
        }
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> getAllByText(String text) {
        String lowerCase = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCase) ||
                        item.getDescription().toLowerCase().contains(lowerCase)) && item.getAvailable())
                .collect(Collectors.toList());
    }
}
