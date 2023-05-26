package com.example.practiceprojectmobile.authentication;

public class AuthenticationResponse {

    private String token;
    private String errorCode;
    private String errorMessage;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, String errorCode, String errorMessage) {
        this.token = token;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
