package com.efgh.revolut.backendtest.service;

import com.efgh.revolut.backendtest.entities.User;
import com.efgh.revolut.backendtest.entities.UserId;
import com.efgh.revolut.backendtest.repositories.UserRepo;

public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User addUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("Invalid user provided");
        }
        if (userRepo.getUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException("There is already a registered user for the provided ID");
        }
        return userRepo.addUser(user);
    }

    public User getUser(UserId userId) {
        User user = userRepo.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("No user registered for the provided ID");
        }
        return user;
    }
}
