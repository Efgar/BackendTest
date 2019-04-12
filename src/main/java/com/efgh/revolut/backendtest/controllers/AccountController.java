package com.efgh.revolut.backendtest.controllers;

import com.efgh.revolut.backendtest.service.AccountService;

import java.util.UUID;

import static com.efgh.revolut.backendtest.utils.JsonUtil.json;
import static spark.Spark.before;
import static spark.Spark.get;

public class AccountController {
    public AccountController(AccountService accountService) {
        before((request, response) -> response.type("application/json"));
        get("/account/:accNumber", (request, response) -> accountService.getAccount(UUID.fromString(request.params(":accNumber"))), json());
    }
}
