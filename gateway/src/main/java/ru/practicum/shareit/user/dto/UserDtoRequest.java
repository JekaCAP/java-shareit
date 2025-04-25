package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDtoRequest {

    private Long id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;
}
