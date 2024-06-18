package com.clientApi.model;

public class AuthResponse {

    public String getJwt() {
        return jwt;
    }

    private String jwt;

    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }
}
