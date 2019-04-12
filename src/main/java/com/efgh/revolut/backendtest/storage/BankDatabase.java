package com.efgh.revolut.backendtest.storage;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Dumb memory storage, no checks for duplicity/null values implemented
 */
public class BankDatabase {
    private static List<Account> bankAccounts = new ArrayList<Account>();
    private static List<User> users = new ArrayList<User>();

    public static Account addAccount(Account account){
        bankAccounts.add(account);
        return account;
    }

    public static Account getAccountByNumber(UUID accountNumber){
        return bankAccounts.stream().parallel().filter(account -> account.getAccountNumber() == accountNumber).findFirst().orElse(null);
    }

    public static List<Account> getUserAccounts(UserId userId) {
        return bankAccounts.stream().parallel().filter(account -> account.isOwnedByUser(userId)).collect(Collectors.toList());
    }

    public static User addUser(User user) {
        users.add(user);
        return user;
    }

    public static User getUserById(UserId userId) {
        return users.stream().parallel().filter(user -> user.getUserId().equals(userId)).findFirst().orElse(null);
    }
}
