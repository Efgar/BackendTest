package com.efgh.revolut.backendtest.repositories;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.storage.BankDatabase;

import java.util.List;
import java.util.UUID;

public class AccountRepoOnMemory implements AccountRepo{
    @Override
    public Account addAccount(Account account) {
        return BankDatabase.addAccount(account);
    }

    @Override
    public Account getAccount(UUID accountNumber) {
        return BankDatabase.getAccountByNumber(accountNumber);
    }

    @Override
    public List<Account> getAccountsByUser(UserId userId) {
        return BankDatabase.getUserAccounts(userId);
    }
}
