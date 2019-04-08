package com.efgh.revolut.backendtest.storage;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.User;

import java.util.ArrayList;
import java.util.List;

public class BankDatabase {
    private static List<Account> bankAccounts = new ArrayList<Account>();
    private static List<User> users = new ArrayList<User>();

    public static void addAccount(Account account){
        if(account == null || account.getAccountNumber() == null){
            throw new IllegalArgumentException("Invalid account added");
        }
        bankAccounts.add(account);
    }
}
