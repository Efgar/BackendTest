package com.efgh.revolut.backendtest.service;


import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.repositories.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.PASSPORT;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepo userRepo;
    private UserService service;

    @BeforeEach
    private void setup(){
        service = new UserService(userRepo);
    }

    @Test
    void addValidUser(){
        UserId userId = new UserId(PASSPORT, "123");
        User user = new User("Jhon Doe", userId);
        lenient().when(userRepo.addUser(user)).thenReturn(user);

        user = service.addUser(user);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getUserId());
    }

    @Test
    void addNullUser(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.addUser(null));
    }

    @Test
    void getUserByValidId(){
        UserId userId = new UserId(PASSPORT, "123");
        User user = new User("Jhon Doe", userId);
        lenient().when(userRepo.getUserById(userId)).thenReturn(user);

        user = service.getUser(userId);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId, user.getUserId());
    }

    @Test
    void getUserByInvalidId(){
        UserId userId = new UserId(PASSPORT, "123");
        lenient().when(userRepo.getUserById(userId)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.addUser(null));
    }

    @Test
    void getUserByNullId(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.addUser(null));
    }
}
