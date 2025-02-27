package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String name;
    @Email
    private String email;

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
