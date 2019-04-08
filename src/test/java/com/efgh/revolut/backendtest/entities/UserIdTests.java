package com.efgh.revolut.backendtest.entities;

import com.efgh.revolut.backendtest.entities.UserId.UserIdType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.OTHER;

public class UserIdTests {

    @Test
    void userIdCreation(){
        String idValue = "123456";
        UserIdType idType = OTHER;
        UserId userId = new UserId(idType, idValue);
        Assertions.assertEquals(idValue, userId.getIdValue());
        Assertions.assertEquals(idType, userId.getType());
    }

    private static Stream<Arguments> userIdCreationInvalidParams() {
        return Stream.of(
                Arguments.of(null, "123456"),
                Arguments.of(OTHER, ""),
                Arguments.of(OTHER, null)
        );
    }

    @ParameterizedTest
    @MethodSource("userIdCreationInvalidParams")
    void userIdCreationInvalid(UserIdType idType, String idValue){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new UserId(idType, idValue));
    }
}
