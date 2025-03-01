package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMemoryRepository userMemoryRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDtoRequest) {
        User user = userMapper.mapToUser(userDtoRequest);
        User savedUser = userMemoryRepository.addUser(user);
        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers() {
        return userMemoryRepository.getUsers().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDtoRequest, Integer userId) {
        User userUpdate = userMapper.mapToUser(userDtoRequest);
        User updatedUser = userMemoryRepository.updateUser(userUpdate, userId);
        return userMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userMemoryRepository.getUserById(userId);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserDto deleteUser(Integer userId) {
        User deletedUser = userMemoryRepository.deleteUser(userId);
        return deletedUser != null ? userMapper.mapToUserDto(deletedUser) : null;
    }
}
