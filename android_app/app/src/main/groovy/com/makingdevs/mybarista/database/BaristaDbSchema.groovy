package com.makingdevs.mybarista.database

class BaristaDbSchema {
    static class UserTable {
        static final String NAME = "users"
        static class Column {
            static final String USERNAME = "username"
            static final String TOKEN = "token"
        }
    }
}