package com.efgh.revolut.backendtest.entities;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class Account {
    private UUID accountNumber;
    private BigDecimal accountBalance;
    private List<User> owners;

    public Account(BigDecimal initialBalance, List<User> accountOwners) {
        accountNumber = UUID.randomUUID();
        accountBalance = defaultIfNull(initialBalance, BigDecimal.ZERO);
        owners = accountOwners;
        validate();
    }

    public void addMoney(BigDecimal value) {
        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new IllegalArgumentException("You can not charge negative values to the account");
        }
        accountBalance = accountBalance.add(value);
    }

    public synchronized void transferMoney(Account destinatary, BigDecimal value) {
        if (iNegativeValue(value) || value.compareTo(getCurrentBalance()) > 0) {
            throw new IllegalStateException();
        }
        if (destinatary == null) {
            throw new IllegalArgumentException("Invalid account specified as beneficiary.");
        }
        accountBalance = accountBalance.subtract(defaultIfNull(value, BigDecimal.ZERO));
        destinatary.addMoney(value);
    }

    public boolean isOwnedByUser(UserId userId){
        return owners.stream().anyMatch(owner -> owner.getUserId().equals(userId));
    }

    public BigDecimal getCurrentBalance() {
        return accountBalance;
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }

    public List<User> getAccountOwners() {
        return owners;
    }

    private boolean iNegativeValue(BigDecimal value) {
        return value != null && BigDecimal.ZERO.compareTo(value) > 0;
    }

    private void validate(){
        if (iNegativeValue(accountBalance)) {
            throw new IllegalArgumentException("Initial balance can not be negative");
        }

        if (CollectionUtils.isEmpty(owners)) {
            throw new IllegalArgumentException("The account must have at least one owner");
        }

        owners.forEach(owner -> {
            if(owner == null){
                throw new IllegalArgumentException("Invalid owner defined for the account");
            }
        });
    }

    @Override
    public boolean equals(Object account) {
        if (account == null) {
            return false;
        }

        if (!(account instanceof Account)) {
            return false;
        }

        if (account == this) {
            return true;
        }

        return this.getAccountNumber() == ((Account) account).getAccountNumber();
    }

    @Override
    public int hashCode() {
        return getAccountNumber().hashCode();
    }
}
