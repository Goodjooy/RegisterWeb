package com.jacky.register.security;

public enum UserRole {
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER");


    private final String name;

    private UserRole(String Name) {
        this.name = Name;
    }

    public String getName() {
        return name;
    }
}
