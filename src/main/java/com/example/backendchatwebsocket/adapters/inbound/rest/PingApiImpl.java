package com.example.backendchatwebsocket.adapters.inbound.rest;

import com.example.backendchat.api.PingApi;
import com.example.backendchat.api.model.PingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class PingApiImpl implements PingApi {

    @Override
    public ResponseEntity<PingResponse> getPing() {
        PingResponse response = new PingResponse();
        response.setMessage("pong");
        return ResponseEntity.ok(response);
    }
}
