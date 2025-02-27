package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody UserDto user, @PathVariable Integer userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

}
