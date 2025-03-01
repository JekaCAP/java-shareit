package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.service.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Repository
public class UserMemoryRepository extends IdGenerator {
    private Map<Integer, User> usersMap = new HashMap<>();

    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("При добавлении не найден email, Укажите email при добавлении.");
            throw new ValidationException("Введите email пользователя.");
        }

        emailValidator(user.getEmail());

        user.setId(getNextId(usersMap));
        usersMap.put(user.getId(), user);
        log.info("Добавлен новый пользователь с id {}", user.getId());
        return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(usersMap.values());
    }

    public User getUserById(Integer userId) {
        return usersMap.get(userId);
    }

    public User updateUser(User userUpdate, Integer userId) {
        if (!usersMap.containsKey(userId)) {
            log.error("Пользователь с id {} не найден.", userId);
            throw new RuntimeException("Пользователь с id " + userId + " не существует.");
        }

        String email = userUpdate.getEmail();
        if (email != null) {
            emailValidator(email);
        }

        User user = usersMap.get(userId);
        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        }
        if (userUpdate.getEmail() != null) {
            user.setEmail(userUpdate.getEmail());
        }

        log.info("Пользователь с id {} обновлен", userId);
        return user;
    }

    public User deleteUser(Integer userId) {
        return usersMap.remove(userId);
    }

    private void emailValidator(String email) {
        usersMap.values().forEach(user -> {
            if (user.getEmail().equals(email)) {
                log.error("Email {} уже используется другим пользователем.", email);
                throw new DuplicatedDataException("Этот email уже занят, попробуйте использовать другой.");
            }
        });
    }
}