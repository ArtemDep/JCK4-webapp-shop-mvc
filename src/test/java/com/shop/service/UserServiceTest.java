package com.shop.service;

import com.shop.dao.UserDao;
import com.shop.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Tests user authentication and registration business logic. */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Test
    void shouldAuthenticateUserWithCorrectPassword() throws Exception {
        User user = new User("artem", "", "artem@example.com");
        user.setId(1L);
        user.setPassword(com.shop.util.PasswordUtil.hashPassword("password"));
        when(userDao.findByUsername("artem")).thenReturn(user);

        UserService service = new UserService(userDao);

        assertEquals(user, service.authenticate("artem", "password"));
        assertNull(service.authenticate("artem", "wrong"));
    }

    @Test
    void shouldRejectDuplicateUsername() throws Exception {
        when(userDao.findByUsername("artem")).thenReturn(new User());
        UserService service = new UserService(userDao);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("artem", "password", "artem@example.com")
        );

        assertEquals("Пользователь уже существует", exception.getMessage());
        verify(userDao, never()).create(any(User.class));
    }
}
