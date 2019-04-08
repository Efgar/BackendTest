package com.efgh.revolut.backendtest.entities;


import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.PESSEL;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class AccountTests {
    private static List<User> singleOwner;

    @BeforeAll
    private static void setup(){
        singleOwner = new ArrayList<>();
        singleOwner.add(new User("Jhon Doe", new UserId(PESSEL, "123456")));
    }

    private static Stream<Arguments> accountCreationParams() {
        List<User> multipleOwners = new ArrayList<>();
        multipleOwners.add(new User("Jhon Doe", new UserId(PESSEL, "123456")));
        multipleOwners.add(new User("Peter Potter", new UserId(PESSEL, "987654")));

        return Stream.of(
                Arguments.of(TEN, singleOwner),
                Arguments.of(null, singleOwner),
                Arguments.of(TEN, singleOwner),
                Arguments.of(ZERO, singleOwner),
                Arguments.of(ZERO, multipleOwners)
        );
    }

    @ParameterizedTest
    @MethodSource("accountCreationParams")
    void accountCreation(BigDecimal initialBalance, List<User> owners) {
        Account newAccount = new Account(initialBalance, owners);
        Assertions.assertNotNull(newAccount);
        Assertions.assertEquals(defaultIfNull(initialBalance, ZERO), newAccount.getCurrentBalance());
        Assertions.assertIterableEquals(owners, newAccount.getAccountOwners());
    }

    private static Stream<Arguments> accountCreationWithInvalidValuesParams() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1), new ArrayList<>()),
                Arguments.of(BigDecimal.ZERO, null),
                Arguments.of(BigDecimal.ZERO, new ArrayList<User>(Collections.singletonList(null)))
        );
    }

    @ParameterizedTest
    @MethodSource("accountCreationWithInvalidValuesParams")
    void accountCreationWithInvalidValues(BigDecimal initialBalance, List<User> owners) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(initialBalance, owners));
    }

    private static Stream<Arguments> chargingAccountParams() {
        return Stream.of(
                Arguments.of(new Account(ZERO, singleOwner), TEN, TEN),
                Arguments.of(new Account(ZERO, singleOwner), ZERO, ZERO),
                Arguments.of(new Account(TEN, singleOwner), TEN, BigDecimal.valueOf(20))
        );
    }

    @ParameterizedTest
    @MethodSource("chargingAccountParams")
    void chargingAccount(Account newAccount, BigDecimal value, BigDecimal expectedValue) {
        newAccount.addMoney(value);
        Assertions.assertEquals(expectedValue, newAccount.getCurrentBalance());
    }

    private static Stream<Arguments> chargingAccountInvalidValueParams() {
        return Stream.of(
                Arguments.of(new Account(ZERO, singleOwner), BigDecimal.valueOf(-10))
        );
    }

    @ParameterizedTest
    @MethodSource("chargingAccountInvalidValueParams")
    void chargingAccountInvalidValue(Account newAccount, BigDecimal value) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> newAccount.addMoney(value));
    }

    private static Stream<Arguments> transferMoneyParams() {
        return Stream.of(
                Arguments.of(new Account(TEN, singleOwner), new Account(ZERO, singleOwner), TEN, ZERO, TEN),
                Arguments.of(new Account(TEN, singleOwner), new Account(TEN, singleOwner), TEN, ZERO, BigDecimal.valueOf(20)),
                Arguments.of(new Account(BigDecimal.valueOf(20), singleOwner), new Account(ZERO, singleOwner), TEN, TEN, TEN),
                Arguments.of(new Account(ZERO, singleOwner), new Account(ZERO, singleOwner), ZERO, ZERO, ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("transferMoneyParams")
    void transferMoney(Account originAccount, Account destinataryAccount, BigDecimal value, BigDecimal expectedBalanceOrigin, BigDecimal expectedBalanceDestination) {
        originAccount.transferMoney(destinataryAccount, value);
        Assertions.assertEquals(expectedBalanceDestination, destinataryAccount.getCurrentBalance());
        Assertions.assertEquals(expectedBalanceOrigin, originAccount.getCurrentBalance());
    }

    @Test
    void transferMoneyInvalidAccount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(TEN, singleOwner).transferMoney(null, TEN));
    }

    private static Stream<Arguments> transferMoneyInvalidAmountParams() {
        return Stream.of(
                Arguments.of(new Account(ZERO, singleOwner), new Account(ZERO, singleOwner), TEN, ZERO, ZERO),
                Arguments.of(new Account(TEN, singleOwner), new Account(TEN, singleOwner), BigDecimal.valueOf(20), TEN, TEN),
                Arguments.of(new Account(ZERO, singleOwner), new Account(ZERO, singleOwner), BigDecimal.valueOf(-10), ZERO, ZERO),
                Arguments.of(new Account(TEN, singleOwner), new Account(ZERO, singleOwner), BigDecimal.valueOf(-10), TEN, ZERO)
        );
    }

    @ParameterizedTest
    @MethodSource("transferMoneyInvalidAmountParams")
    void transferMoneyInvalidAmount(Account originAccount, Account destinataryAccount, BigDecimal value, BigDecimal expectedBalanceOrigin, BigDecimal expectedBalanceDestination) {
        Assertions.assertThrows(IllegalStateException.class, () -> originAccount.transferMoney(destinataryAccount, value));
        Assertions.assertEquals(expectedBalanceDestination, destinataryAccount.getCurrentBalance());
        Assertions.assertEquals(expectedBalanceOrigin, originAccount.getCurrentBalance());
    }
}
