package com.randomrainbow.springboot.demosecurity.entity;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static Role fromString(String roleString) {
        for (Role role : values()) {
            if (role.toString().equalsIgnoreCase(roleString)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + roleString);
    }
}