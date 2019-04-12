package com.efgh.revolut.backendtest;

import static spark.Spark.get;

public class ApplicationMain {

    public static void main(String[] args) {
        get("/hello", (request, response) -> "world");
    }

}