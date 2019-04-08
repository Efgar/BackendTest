package com.efgh.revolut.backendtest.entities;

public class UserId {
    private UserIdType type;
    private String idValue;

    public enum UserIdType {
        PASSPORT,
        PESSEL,
        OTHER;
    }
}
