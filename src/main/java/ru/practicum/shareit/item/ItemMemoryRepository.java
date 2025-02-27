package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.service.IdGenerator;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMemoryRepository;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ItemMemoryRepository extends IdGenerator {
    private final ItemMapper itemMapper;
    private final UserMemoryRepository userMemoryRepository;
    Map<Integer, Item> itemsMap = new HashMap<>();

    public ItemDto addNewItem(ItemDto itemDtoRequest, Integer userId) {

        if (userMemoryRepository.getUserById(userId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует.");
        }

        Item item = itemMapper.mapToItem(itemDtoRequest);
        item.setOwner(userMemoryRepository.getUserById(userId));
        item.setId(getNextId(itemsMap));
        itemsMap.put(item.getId(), item);

        log.info("Предмет c id {} добавлен пользователем с id {}.", item.getId(), userId);
        return itemMapper.mapToItemDto(item);
    }

    public ItemDto updateItem(Integer itemId, ItemDtoUpdate itemDtoRequest, Integer userId) {
        if (!itemsMap.containsKey(itemId)) {
            log.error("Предмет с id {} не найден.", itemId);
            throw new NotFoundException("Предмет с таким id не найден.");
        }

        Integer itemsOwnerId = itemsMap.get(itemId).getOwner().getId();
        if (!Objects.equals(itemsOwnerId, userId)) {
            log.error("Пользователь с id {} не является владельцем предмета с id {}.", userId, itemsOwnerId);
            throw new AccessDeniedException("Только владелец может изменить предмет.");
        }

        Item updatedItem = new Item();

        updatedItem.setName(itemDtoRequest.getName());
        updatedItem.setDescription(itemDtoRequest.getDescription());
        updatedItem.setAvailable(itemDtoRequest.getAvailable());

        itemsMap.put(itemId, updatedItem);

        log.info("Предмет с id {} обновлен.", itemId);
        return itemMapper.mapToItemDto(updatedItem);
    }

    public ItemDto getItem(Integer itemId) {
        if (!itemsMap.containsKey(itemId)) {
            log.error("Предмет с id {} не найден.", itemId);
            throw new NotFoundException("Предмет с таким id не существует.");
        }

        log.info("Предмет найден.");
        return itemMapper.mapToItemDto(itemsMap.get(itemId));
    }

    public List<ItemDto> getOwnerItems(Integer ownerId) {
        Collection<Item> allItems = itemsMap.values();
        List<ItemDto> ownerItemsList = new ArrayList<>();

        allItems.forEach(item -> {
            User owner = item.getOwner();
            if (owner != null) {
                if (Objects.equals(owner.getId(), ownerId)) {
                    ownerItemsList.add(itemMapper.mapToItemDto(item));
                }
            }
        });

        if (ownerItemsList.isEmpty()) {
            log.error("Нет предметов для пользователя с id {}.", ownerId);
            throw new NotFoundException("У пользователя нет предметов.");
        }

        log.info("Предметы для пользователя с id {} найдены.");
        return ownerItemsList;
    }

    public List<ItemDto> itemSearch(String text) {
        List<ItemDto> searchItems = new ArrayList<>();
        if (text != null && !text.isBlank()) {

            Collection<Item> allItems = itemsMap.values();

            allItems.forEach(item -> {
                if (item.getName() != null && item.getAvailable() != null && item.getAvailable().equals(true)) {
                    if ((item.getName().toLowerCase().contains(text.toLowerCase()))) {
                        searchItems.add(itemMapper.mapToItemDto(item));
                        log.info("Предмет найден по названию. Название вещи: {}, текст поиска: {}, id: {}",
                                item.getName(), text, item.getId());
                    }
                }

                if (item.getDescription() != null && item.getAvailable() != null && item.getAvailable().equals(true)) {
                    if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        searchItems.add(itemMapper.mapToItemDto(item));
                        log.info("редмет найден по описанию. Описанию: {}, текст поиска {}, id {}",
                                item.getDescription(), text, item.getId());
                    }
                }
            });

            System.out.println("текст: " + text);
        }
        return searchItems;
    }
}

