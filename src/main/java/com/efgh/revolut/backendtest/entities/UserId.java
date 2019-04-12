package com.efgh.revolut.backendtest.entities;

import org.apache.commons.lang3.StringUtils;

public class UserId {
    private UserIdType type;
    private String idValue;

    public UserId(UserIdType type, String idValue) {
        if(StringUtils.isBlank(idValue) || type == null){
            throw new IllegalArgumentException("Not valid parameters for user id creation");
        }
        this.type = type;
        this.idValue = idValue;
    }

    public UserIdType getType() {
        return type;
    }

    public void setType(UserIdType type) {
        this.type = type;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }


    public enum UserIdType {
        PASSPORT,
        PESSEL,
        OTHER
    }
}
