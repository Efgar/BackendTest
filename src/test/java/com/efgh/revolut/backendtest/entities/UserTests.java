package com.efgh.revolut.backendtest.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.OTHER;
import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.PESSEL;

public class UserTests {


    private static Stream<Arguments> userCreationParams() {
        return Stream.of(
                Arguments.of("Jhon Doe", new UserId(PESSEL, "123456")),
                Arguments.of("Petter potter", new UserId(OTHER, "987654"))
        );
    }

    @ParameterizedTest
    @MethodSource("userCreationParams")
    void userCreation(String userName, UserId userId){
        User user = new User(userName, userId);
        Assertions.assertEquals(userName, user.getName());
        Assertions.assertEquals(userId, user.getUserId());
        Assertions.assertNull(user.getEmail());
    }

    private static Stream<Arguments> userCreationWithEmailParams() {
        return Stream.of(
                Arguments.of("Jhon Doe", new UserId(PESSEL, "123456"), null),
                Arguments.of("Petter potter", new UserId(OTHER, "987654"), "myemail@email.com")
        );
    }

    @ParameterizedTest
    @MethodSource("userCreationWithEmailParams")
    void userCreationWithEmail(String userName, UserId userId, String email){
        User user = new User(userName, userId, email);
        Assertions.assertEquals(userName, user.getName());
        Assertions.assertEquals(userId, user.getUserId());
        Assertions.assertEquals(email, user.getEmail());
    }

    private static Stream<Arguments> userCreationInvalidArgsParams() {
        return Stream.of(
                Arguments.of("Jhon Doe", null, null),
                Arguments.of(null, new UserId(PESSEL, "123456"), null),
                Arguments.of("", new UserId(PESSEL, "123456"), null),
                Arguments.of("Petter potter", new UserId(OTHER, "987654"), "notAnEmail")
        );
    }

    @ParameterizedTest
    @MethodSource("userCreationInvalidArgsParams")
    void userCreationInvalidArgs(String userName, UserId userId, String email){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new User(userName, userId, email));
    }
}
