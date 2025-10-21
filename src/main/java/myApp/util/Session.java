package main.java.myApp.util;

import main.java.myApp.model.User;

/**
 * This is for keeping track as to who's using the app
 */
public class Session {
    private static User currentUser;

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}

