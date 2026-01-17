package com.example.backendchatwebsocket.application.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat.oauth.yandex")
public class YandexOAuthProperties {
    private String clientId;
    private String redirectUri;
    private String tokenPageOrigin;
    private String userInfoUri = "https://login.yandex.ru/info?format=json";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getTokenPageOrigin() {
        return tokenPageOrigin;
    }

    public void setTokenPageOrigin(String tokenPageOrigin) {
        this.tokenPageOrigin = tokenPageOrigin;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }
}
