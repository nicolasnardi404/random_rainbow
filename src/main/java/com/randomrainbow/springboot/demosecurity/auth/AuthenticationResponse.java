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

    private String accessToken;
    private String refreshToken;
    private String errorMessage; // Optional field for error messages

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public AuthenticationResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
