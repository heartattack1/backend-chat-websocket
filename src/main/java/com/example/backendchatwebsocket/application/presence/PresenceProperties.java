package com.example.backendchatwebsocket.application.presence;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat.presence")
public class PresenceProperties {
    private List<String> usernameHeaderNames = List.of("username", "author");
    private String guestPrefix = "guest-";

    public List<String> getUsernameHeaderNames() {
        return usernameHeaderNames;
    }

    public void setUsernameHeaderNames(List<String> usernameHeaderNames) {
        this.usernameHeaderNames = usernameHeaderNames;
    }

    public String getGuestPrefix() {
        return guestPrefix;
    }

    public void setGuestPrefix(String guestPrefix) {
        this.guestPrefix = guestPrefix;
    }
}
