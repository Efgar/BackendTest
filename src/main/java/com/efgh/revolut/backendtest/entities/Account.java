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
    private List<User> owners = new ArrayList<>();

    public Account(BigDecimal initialBalance, List<User> owners) {
        if (iNegativeValue(initialBalance)) {
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

        this.accountNumber = UUID.randomUUID();
        accountBalance = defaultIfNull(initialBalance, BigDecimal.ZERO);
        this.owners.addAll(owners);
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
