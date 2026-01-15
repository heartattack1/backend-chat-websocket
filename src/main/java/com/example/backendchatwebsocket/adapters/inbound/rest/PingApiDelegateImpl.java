package com.example.backendchatwebsocket.adapters.inbound.rest;

import com.example.backendchat.api.model.PingResponse;
import com.example.backendchat.api.PingApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PingApiDelegateImpl implements PingApiDelegate {

    @Override
    public ResponseEntity<PingResponse> getPing() {
        PingResponse response = new PingResponse();
        response.setMessage("pong");
        return ResponseEntity.ok(response);
    }
}
