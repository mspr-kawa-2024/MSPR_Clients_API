package com.clientApi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthResponseTest {

    @Test
    public void testAuthResponseConstructor() {
        String jwtToken = "testJwtToken";
        AuthResponse authResponse = new AuthResponse(jwtToken);

        assertNotNull(authResponse);
        assertEquals(jwtToken, authResponse.getJwt());
    }

    @Test
    public void testGetJwt() {
        String jwtToken = "testJwtToken";
        AuthResponse authResponse = new AuthResponse(jwtToken);

        String result = authResponse.getJwt();
        assertEquals(jwtToken, result);
    }
}
