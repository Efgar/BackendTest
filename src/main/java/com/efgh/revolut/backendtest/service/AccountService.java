package com.efgh.revolut.backendtest.service;

import com.efgh.revolut.backendtest.entities.Account;
import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.repositories.AccountRepo;
import com.efgh.revolut.backendtest.storage.BankDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AccountService {

    AccountRepo accountRepo;

    public Account createAccount(List<User> owners){
        Account account = new Account(BigDecimal.ZERO, owners);
        return accountRepo.addAccount(account);
    }

    public List<Account> getAccounts(UserId owner){
        return accountRepo.getAccountsByUser(owner);
    }

    public Account getAccount(UUID accountNumber){
        Account account = accountRepo.getAccount(accountNumber);
        if(account == null){
            throw new IllegalArgumentException("No account with the given number found");
        }
        return account;
    }

    public Account transferMoney(UUID originAccountNumber, UUID destinationAccountNumber, BigDecimal value){
        Account origin = getAccount(originAccountNumber);
        origin.transferMoney(accountRepo.getAccount(destinationAccountNumber), value);
        return origin;
    }

}
