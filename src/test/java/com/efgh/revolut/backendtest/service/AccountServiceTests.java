package com.efgh.revolut.backendtest.service;


import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.repositories.AccountRepo;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.PASSPORT;
import static com.efgh.revolut.backendtest.entities.UserId.UserIdType.PESSEL;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests {

    @Mock
    private AccountRepo accountRepo;
    private AccountService service;
    private List<User> singleOwner;
    private List<Account> mockedAccounts;

    @BeforeEach
    private void setup(){
        service = new AccountService(accountRepo);
        singleOwner = new ArrayList<>();
        singleOwner.add(new User("Jhon Doe", new UserId(PESSEL, "123456")));

        mockedAccounts = new ArrayList<>();
        mockedAccounts.add(new Account(ZERO, singleOwner));
    }

    @Test
    void createAccountWithValidAccount() {
        Account newAccount = new Account(ZERO, singleOwner);
        lenient().when(accountRepo.addAccount(newAccount)).thenReturn(newAccount);

        newAccount = service.createAccount(newAccount);

        Assertions.assertNotNull(newAccount);
        Assertions.assertEquals(ZERO, newAccount.getCurrentBalance());
        Assertions.assertIterableEquals(singleOwner, newAccount.getAccountOwners());
    }

    @Test
    void createAccountWithNullAccount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.createAccount(null));
    }

    @Test
    void getAccountsForValidUser() {
        UserId userId = new UserId(PASSPORT, "123");
        lenient().when(accountRepo.getAccountsByUser(userId)).thenReturn(mockedAccounts);

        List<Account> accounts = service.getAccounts(userId);

        Assertions.assertNotNull(accounts);
        Assertions.assertIterableEquals(mockedAccounts, accounts);
    }

    @Test
    void getAccountsReturningEmpty() {
        UserId userId = new UserId(PASSPORT, "321");
        lenient().when(accountRepo.getAccountsByUser(userId)).thenReturn(new ArrayList<>());

        List<Account> accounts = service.getAccounts(userId);

        Assertions.assertNotNull(accounts);
        Assertions.assertTrue(CollectionUtils.isEmpty(accounts));
    }

    @Test
    void getAccountsForNullUser() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAccounts(null));
    }

    @Test
    void getAccountForValidId(){
        Account account = new Account(TEN, singleOwner);
        UUID accountNumber = account.getAccountNumber();
        lenient().when(accountRepo.getAccount(accountNumber)).thenReturn(account);

        account = service.getAccount(accountNumber);

        Assertions.assertNotNull(account);
        Assertions.assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    void getAccountForInvalidId(){
        UUID accountNumber = UUID.randomUUID();

        lenient().when(accountRepo.getAccount(accountNumber)).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAccount(accountNumber));
    }

    @Test
    void getAccountForNullId(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAccount(null));
    }

    @Test
    void transferMoneyValidAccounts(){
        UUID originAccountNumber = UUID.randomUUID();
        Account originAccount = new Account(TEN, singleOwner);
        lenient().when(accountRepo.getAccount(originAccountNumber)).thenReturn(originAccount);

        UUID destinationAccountNumber = UUID.randomUUID();
        Account destinationAccount = new Account(ZERO, singleOwner);;
        lenient().when(accountRepo.getAccount(destinationAccountNumber)).thenReturn(destinationAccount);

        originAccount = service.transferMoney(originAccountNumber, destinationAccountNumber, TEN);

        Assertions.assertNotNull(originAccount);
        Assertions.assertEquals(ZERO, originAccount.getCurrentBalance());
        Assertions.assertEquals(TEN, destinationAccount.getCurrentBalance());
    }

    private static Stream<Arguments> transferMoneyInvalidAccountsParams() {
        return Stream.of(
                Arguments.of(null, null, TEN),
                Arguments.of(null, UUID.randomUUID(), TEN),
                Arguments.of(UUID.randomUUID(), null, TEN)
        );
    }

    @ParameterizedTest
    @MethodSource("transferMoneyInvalidAccountsParams")
    void transferMoneyInvalidAccounts(UUID originAccount, UUID destinationAccount, BigDecimal value) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.transferMoney(originAccount, destinationAccount, value));
    }

    @Test
    void chargeAccount(){
        UUID accountNumber = UUID.randomUUID();
        Account account = new Account(ZERO, singleOwner);

        lenient().when(accountRepo.getAccount(accountNumber)).thenReturn(account);

        account = service.chargeAccount(accountNumber, TEN);
        Assertions.assertNotNull(account);
        Assertions.assertEquals(TEN, account.getCurrentBalance());
    }

    @Test
    void chargeAccountForNullAccount(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.chargeAccount(null, TEN));
    }
}
