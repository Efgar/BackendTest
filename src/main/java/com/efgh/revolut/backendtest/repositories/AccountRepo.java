package com.efgh.revolut.backendtest.repositories;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.UserId;

import java.util.List;
import java.util.UUID;

public interface AccountRepo {
    Account addAccount(Account account);
    Account getAccount(UUID accountNumber);
    List<Account> getAccountsByUser(UserId userId);
}
