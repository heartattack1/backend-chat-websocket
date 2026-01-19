package com.example.backendchatwebsocket.infrastructure.time;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfiguration {

    @Bean
    public ZoneId appZoneId(AppTimeProperties properties) {
        return ZoneId.of(properties.timezone());
    }

    @Bean
    public Clock clock(ZoneId appZoneId) {
        return Clock.system(appZoneId);
    }
}
