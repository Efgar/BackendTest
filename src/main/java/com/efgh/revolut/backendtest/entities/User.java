package com.efgh.revolut.backendtest.entities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class User {
    private UserId userId;
    private String name;
    private String email;

    public User(String name, UserId userId) {
        this(name, userId, null);
    }

    public User(String name, UserId userId, String email) {
        if (StringUtils.isBlank(name) || userId == null || (email != null && !EmailValidator.getInstance().isValid(email))) {
            throw new IllegalArgumentException("Not valid parameters for user creation");
        }
        this.name = name;
        this.userId = userId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object user) {
        if (user == null) {
            return false;
        }

        if (!(user instanceof User)) {
            return false;
        }

        if (user == this) {
            return true;
        }

        return this.getUserId() == ((User) user).getUserId();
    }
}
