package com.efgh.revolut.backendtest.repositories;

import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;

public interface UserRepo {
    User addUser(User user);
    User getUserById(UserId userId);
}
