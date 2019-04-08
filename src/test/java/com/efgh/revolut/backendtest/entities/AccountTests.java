package com.efgh.revolut.backendtest.entities;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class AccountTests {

    private static Stream<Arguments> accountCreationParams() {
        List<User> singleOwner = new ArrayList<>();
        singleOwner.add(new User());

        List<User> multipleOwners = new ArrayList<>();
        multipleOwners.add(new User());

        return Stream.of(
                Arguments.of(BigDecimal.TEN,singleOwner),
                Arguments.of(null, singleOwner),
                Arguments.of(BigDecimal.TEN, singleOwner),
                Arguments.of(BigDecimal.ZERO, singleOwner),
                Arguments.of(BigDecimal.ZERO, multipleOwners)
        );
    }

    @ParameterizedTest
    @MethodSource("accountCreationParams")
    void accountCreation(BigDecimal initialBalance, List<User> owners){
        Account newAccount = new Account(initialBalance, owners);
        Assertions.assertNotNull(newAccount);
        Assertions.assertEquals(defaultIfNull(initialBalance, BigDecimal.ZERO), newAccount.getCurrentBalance());
        Assertions.assertIterableEquals(owners, newAccount.getAccountOwners());
    }

    private static Stream<Arguments> accountCreationWithInvalidValuesParams() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1), new ArrayList<>()),
                Arguments.of(BigDecimal.valueOf(-1), null)
        );
    }
 
    @ParameterizedTest()
    @MethodSource("accountCreationWithInvalidValuesParams")
    void accountCreationWithInvalidValues(BigDecimal initialBalance, List<User> owners){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Account(initialBalance, owners));
    }

    private static Stream<Arguments> chargingAccountParamaters(){
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1), new ArrayList<>()),
                Arguments.of(BigDecimal.valueOf(-1), null)
        );
    }

    public void chargingAccount(Account newAccount, BigDecimal value, BigDecimal expectedValue){
        newAccount.addMoney(value);
        Assertions.assertEquals(expectedValue, newAccount.getCurrentBalance());
    }

    private static Stream<Arguments> chargingAccountInvalidValue(){
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1), new ArrayList<>()),
                Arguments.of(BigDecimal.valueOf(-1), null)
        );
    }

    public void testAccountEqualityWithEqualAccounts(){

    }

    public void testAccountEqualityWithNotEqualAccounts(){

    }

}
