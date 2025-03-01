package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.service.IdGenerator;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMemoryRepository;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ItemMemoryRepository extends IdGenerator {
    private final UserMemoryRepository userMemoryRepository;
    private final Map<Integer, Item> itemsMap = new HashMap<>();

    public Item addNewItem(Item item, Integer userId) {
        User owner = userMemoryRepository.getUserById(userId);
        if (owner == null) {
            throw new NotFoundException("Пользователя с таким id не существует.");
        }

        item.setOwner(owner);
        item.setId(getNextId(itemsMap));
        itemsMap.put(item.getId(), item);

        log.info("Предмет c id {} добавлен пользователем с id {}.", item.getId(), userId);
        return item;
    }

    public Item updateItem(Integer itemId, Item itemUpdate, Integer userId) {
        Item existingItem = itemsMap.get(itemId);
        if (existingItem == null) {
            log.error("Предмет с id {} не найден.", itemId);
            throw new NotFoundException("Предмет с таким id не найден.");
        }

        if (!Objects.equals(existingItem.getOwner().getId(), userId)) {
            log.error("Пользователь с id {} не является владельцем предмета с id {}.", userId, itemId);
            throw new AccessDeniedException("Только владелец может изменить предмет.");
        }

        if (itemUpdate.getName() != null) {
            existingItem.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null) {
            existingItem.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            existingItem.setAvailable(itemUpdate.getAvailable());
        }

        log.info("Предмет с id {} обновлен.", itemId);
        return existingItem;
    }

    public Item getItem(Integer itemId) {
        Item item = itemsMap.get(itemId);
        if (item == null) {
            log.error("Предмет с id {} не найден.", itemId);
            throw new NotFoundException("Предмет с таким id не существует.");
        }

        log.info("Предмет с id {} найден.", itemId);
        return item;
    }

    public List<Item> getOwnerItems(Integer ownerId) {
        List<Item> ownerItemsList = new ArrayList<>();

        for (Item item : itemsMap.values()) {
            if (item.getOwner() != null && Objects.equals(item.getOwner().getId(), ownerId)) {
                ownerItemsList.add(item);
            }
        }

        if (ownerItemsList.isEmpty()) {
            log.error("Нет предметов для пользователя с id {}.", ownerId);
            throw new NotFoundException("У пользователя нет предметов.");
        }

        log.info("Найдено {} предметов для пользователя с id {}.", ownerItemsList.size(), ownerId);
        return ownerItemsList;
    }

    public List<Item> itemSearch(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return searchItems;
        }

        String searchText = text.toLowerCase();

        for (Item item : itemsMap.values()) {
            if (Boolean.TRUE.equals(item.getAvailable())) {
                if (item.getName() != null && item.getName().toLowerCase().contains(searchText)) {
                    searchItems.add(item);
                    log.info("Предмет найден по названию. Название: {}, текст поиска: {}, id: {}",
                            item.getName(), text, item.getId());
                } else if (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText)) {
                    searchItems.add(item);
                    log.info("Предмет найден по описанию. Описание: {}, текст поиска: {}, id: {}",
                            item.getDescription(), text, item.getId());
                }
            }
        }

        return searchItems;
    }
}
