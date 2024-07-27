package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private static final int MAX_SIZE_NAME = 30;
    private static final int MAX_SIZE_DESCRIPTION = 150;

    @Override
    public Collection<ItemDto> getAllByUsersId(long userId) {
        checkUserExistence(userId, "GET-ALL-ITEMS");
        Collection<Item> items = itemRepository.findByUserId(userId);
        return items.stream()
                .map(ItemMapper.INSTANCE::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("GET-ITEM-BY-ID Предмет с id={} не найден", itemId);
                    return new NotFoundException("Предмета с id=" + itemId + " не существует");
                });
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto add(long userId, ItemCreateDto itemCreateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("ADD-ITEM Пользователь с id={} не найден", userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
        Item item = ItemMapper.INSTANCE.toItem(itemCreateDto);
        item.setOwner(user);
        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto update(long userId, long itemId, ItemUpdateDto itemUpdateDto) {
        checkUserExistence(userId, "UPDATE-ITEM");
        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("UPDATE-ITEM Предмет с id={} не найден", itemId);
                    return new NotFoundException("Предмета с id=" + itemId + " не существует");
                });
        Item item = ItemMapper.INSTANCE.toItem(itemUpdateDto);
        if (userId != (updatedItem.getOwner().getId())) {
            throw new NotFoundException("The user's ID is different from the owner's ID");
        }

        if (item.getName() != null && !item.getName().isBlank() && item.getName().length() <= MAX_SIZE_NAME) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()
                && item.getDescription().length() <= MAX_SIZE_DESCRIPTION) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(updatedItem));
    }

    @Override
    @Transactional(readOnly = true)
    public void delete(long userId, long itemId) {
        checkUserExistence(userId, "DELETE-ITEM");
        checkItemExistence(itemId, "DELETE-ITEM");
        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    @Override
    public Collection<ItemDto> getAllByText(String text) {
        Collection<Item> items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text,
                text);
        return items.stream().map(ItemMapper.INSTANCE::toItemDto).collect(Collectors.toList());
    }


    private void checkUserExistence(Long userId, String method) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("{} Пользователь с id={} не найден", method, userId);
                    return new NotFoundException("Пользователя с id=" + userId + " не существует");
                });
    }

    private void checkItemExistence(Long itemId, String method) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("{} Предмет с id={} не найден", method, itemId);
                    return new NotFoundException("Предмета с id=" + itemId + " не существует");
                });
    }
}
