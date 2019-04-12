package com.efgh.revolut.backendtest.service;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.repositories.AccountRepo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AccountService {

    private AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account createAccount(Account account){
        if(account == null){
            throw new IllegalArgumentException("Invalid parameters for account creation");
        }
        return accountRepo.addAccount(account);
    }

    public List<Account> getAccounts(UserId owner){
        if(owner == null){
            throw new IllegalArgumentException("Invalid user id provided.");
        }
        return accountRepo.getAccountsByUser(owner);
    }

    public Account getAccount(UUID accountNumber){
        if(accountNumber == null){
            throw new IllegalArgumentException("Invalid account number provided.");
        }
        Account account = accountRepo.getAccount(accountNumber);
        if(account == null){
            throw new IllegalArgumentException(String.format("No account with the given number [%s] found", accountNumber));
        }
        return account;
    }

    public Account transferMoney(UUID originAccountNumber, UUID destinationAccountNumber, BigDecimal value){
        Account origin = getAccount(originAccountNumber);
        origin.transferMoney(getAccount(destinationAccountNumber), value);
        return origin;
    }

    public Account chargeAccount(UUID accountNumber, BigDecimal value){
        Account origin = getAccount(accountNumber);
        origin.addMoney(value);
        return origin;
    }

}
