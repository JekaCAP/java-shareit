package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMemoryRepository itemMemoryRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addNewItem(ItemDto itemDtoRequest, Integer userId) {
        Item item = itemMapper.mapToItem(itemDtoRequest);
        Item savedItem = itemMemoryRepository.addNewItem(item, userId);
        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Integer itemId, ItemDtoUpdate itemDtoRequest, Integer userId) {
        Item updatedItem = new Item();
        updatedItem.setName(itemDtoRequest.getName());
        updatedItem.setDescription(itemDtoRequest.getDescription());
        updatedItem.setAvailable(itemDtoRequest.getAvailable());

        Item savedItem = itemMemoryRepository.updateItem(itemId, updatedItem, userId);
        return itemMapper.mapToItemDto(savedItem);
    }

    public ItemDto getItem(Integer itemId) {
        Item item = itemMemoryRepository.getItem(itemId);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getOwnerItems(Integer ownerId) {
        return itemMemoryRepository.getOwnerItems(ownerId).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> itemSearch(String text) {
        return itemMemoryRepository.itemSearch(text).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }
}
