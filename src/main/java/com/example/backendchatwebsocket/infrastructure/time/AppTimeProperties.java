package com.example.backendchatwebsocket.infrastructure.time;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app")
public record AppTimeProperties(@DefaultValue("UTC") String timezone) {}
