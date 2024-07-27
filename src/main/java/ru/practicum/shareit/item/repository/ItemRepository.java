package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteByOwnerIdAndId(long ownerId, long itemId);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))" +
            "AND i.available = true")
    Collection<Item> findAllByText(String text);

    @Query("SELECT i FROM Item i WHERE i.owner.id = :id")
    Collection<Item> findByUserId(Long id);

    Collection<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}