package com.ssaa.auth.enums;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    READONLY("READONLY");
    
    private final String value;
    
    Role(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}