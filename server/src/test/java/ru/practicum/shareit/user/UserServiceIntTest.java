package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private UserService service;

    @Test
    void createUserTest() {
        UserDtoRequest userDtoRequest = new UserDtoRequest(null, "name", "mail@mail.ru");
        service.createUser(userDtoRequest);

        TypedQuery<User> query = em.createQuery("select us from User as us where us.name = :name", User.class);
        User user = query.setParameter("name", userDtoRequest.getName()).getSingleResult();

        assertNotNull(user.getId());
        assertEquals(user.getName(), userDtoRequest.getName());
        assertEquals(user.getEmail(), userDtoRequest.getEmail());
        service.deleteUser(user.getId());
    }

    @Test
    void getUserTest() {
        UserDtoRequest userDtoRequest = new UserDtoRequest(null, "name", "mail@mail.ru");
        UserDtoResponse createUser = service.createUser(userDtoRequest);
        UserDtoResponse readUser = service.getUser(createUser.getId());

        assertNotNull(createUser.getId());
        assertNotNull(readUser.getId());
        assertEquals(createUser.getId(), readUser.getId());
        assertEquals(createUser.getEmail(), readUser.getEmail());
        assertEquals(createUser.getName(), readUser.getName());
        service.deleteUser(createUser.getId());
    }

    @Test
    void getUsersTest_Throw() {
        Throwable exception = assertThrows(
                NotFoundException.class,
                () -> service.getUsers()
        );
        assertEquals("Лист пользователей совершенно пуст", exception.getMessage());
    }


}
