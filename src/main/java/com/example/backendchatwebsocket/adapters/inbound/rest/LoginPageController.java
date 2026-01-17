package com.example.backendchatwebsocket.adapters.inbound.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {
    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/oauth/token")
    public String token() {
        return "forward:/oauth/token.html";
    }
}
