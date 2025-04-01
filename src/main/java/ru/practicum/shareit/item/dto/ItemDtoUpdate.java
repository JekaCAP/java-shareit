package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDtoUpdate {
    private Integer ownerId;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;

    public boolean hasOwnerId() {
        return ownerId != null;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasAvailable() {
        return available != null;
    }

    public boolean hasRequestId() {
        return requestId != null;
    }
}