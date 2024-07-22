package com.randomrainbow.springboot.demosecurity.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String errorMessage; // Optional field for error messages

    // Constructor with token only
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
