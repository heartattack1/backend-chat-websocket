package com.example.backendchatwebsocket.adapters.inbound.rest;

import com.example.backendchatwebsocket.application.auth.YandexOAuthProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class AuthController {
    private static final String OAUTH_HEADER_PREFIX = "OAuth ";

    private final YandexOAuthProperties yandexOAuthProperties;
    private final RestClient restClient;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public AuthController(YandexOAuthProperties yandexOAuthProperties, RestClient.Builder restClientBuilder) {
        this.yandexOAuthProperties = yandexOAuthProperties;
        this.restClient = restClientBuilder.build();
    }

    @GetMapping("/api/auth/config")
    public ResponseEntity<AuthConfigResponse> getAuthConfig() {
        if (yandexOAuthProperties.getClientId() == null || yandexOAuthProperties.getClientId().isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(new AuthConfigResponse(
                yandexOAuthProperties.getClientId(),
                yandexOAuthProperties.getRedirectUri(),
                yandexOAuthProperties.getTokenPageOrigin()
        ));
    }

    @PostMapping(path = "/api/auth/yandex", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrentUserResponse> loginWithYandex(
            @RequestBody TokenRequest tokenRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String accessToken = Optional.ofNullable(tokenRequest)
                .map(TokenRequest::accessToken)
                .map(String::trim)
                .orElse("");
        if (accessToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        YandexUserInfo userInfo;
        try {
            userInfo = fetchUserInfo(accessToken);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = resolveUsername(userInfo);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        var context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok(new CurrentUserResponse(username, Instant.now().toString()));
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<CurrentUserResponse> currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        return ResponseEntity.ok(new CurrentUserResponse(username, Instant.now().toString()));
    }

    private YandexUserInfo fetchUserInfo(String accessToken) {
        return restClient.get()
                .uri(yandexOAuthProperties.getUserInfoUri())
                .header("Authorization", OAUTH_HEADER_PREFIX + accessToken)
                .retrieve()
                .onStatus(status -> status.isError(), (request, response) -> {
                    throw new IllegalArgumentException("Invalid OAuth token");
                })
                .body(YandexUserInfo.class);
    }

    private String resolveUsername(YandexUserInfo userInfo) {
        if (userInfo == null) {
            return "unknown";
        }
        if (userInfo.displayName() != null && !userInfo.displayName().isBlank()) {
            return userInfo.displayName().trim();
        }
        if (userInfo.realName() != null && !userInfo.realName().isBlank()) {
            return userInfo.realName().trim();
        }
        if (userInfo.login() != null && !userInfo.login().isBlank()) {
            return userInfo.login().trim();
        }
        if (userInfo.id() != null && !userInfo.id().isBlank()) {
            return userInfo.id().trim();
        }
        return "unknown";
    }

    public record TokenRequest(@JsonProperty("access_token") String accessToken) {}

    public record AuthConfigResponse(String clientId, String redirectUri, String tokenPageOrigin) {}

    public record CurrentUserResponse(String username, String loggedInAt) {}

    public record YandexUserInfo(
            String id,
            String login,
            @JsonProperty("display_name") String displayName,
            @JsonProperty("real_name") String realName
    ) {}
}
