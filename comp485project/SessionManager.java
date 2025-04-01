package com.example.comp485project;

public class SessionManager {
    private static String loggedInUsername;

    public static void setUsername(String username) {
        loggedInUsername = username;
    }

    public static String getUsername() {
        return loggedInUsername;
    }
}
