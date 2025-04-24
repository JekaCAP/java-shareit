package ru.practicum.shareit.user.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String USER_NOT_EXIST = "Пользователя с ID %s не существует";
    private final UserStorage userStorage;

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        if (userDtoRequest.getEmail() == null || !userDtoRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new BadRequestException("Email обязателен и должен быть в корректном формате");
        }
        User user = userStorage.save(UserMapper.toUser(userDtoRequest));
        log.info("User Service: Пользователь создан. Его ID {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDtoResponse updateUser(UserDtoRequest userDtoRequest) {
        User user = userStorage.findById(userDtoRequest.getId())
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXIST, userDtoRequest.getId())));
        if (StringUtils.isNotBlank(userDtoRequest.getName())) {
            user.setName(userDtoRequest.getName());
        }
        if (StringUtils.isNotBlank(userDtoRequest.getEmail())) {
            user.setEmail(userDtoRequest.getEmail());
        }
        log.info("User Service: Пользователь обновлен. Его ID {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDtoResponse deleteUser(long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXIST, userId)));
        userStorage.deleteById(userId);
        log.info("User Service: Пользователь удален. Его ID {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUser(long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_EXIST, userId)));
        log.info("User Service: Пользователь найден. Его ID {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getUsers() {
        List<User> result = userStorage.findAll();
        if (!result.isEmpty()) {
            log.info("User Service: Найдены пользователи. Количество: {}", result.size());
            return UserMapper.toUserDtoList(result);
        }
        throw new NotFoundException("Лист пользователей совершенно пуст");
    }
}