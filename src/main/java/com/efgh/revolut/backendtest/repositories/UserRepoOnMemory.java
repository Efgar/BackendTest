package com.efgh.revolut.backendtest.repositories;

import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.storage.BankDatabase;

public class UserRepoOnMemory implements UserRepo{
    @Override
    public User addUser(User user) {
        return BankDatabase.addUser(user);
    }

    @Override
    public User getUserById(UserId userId) {
        return BankDatabase.getUserById(userId);
    }
}
