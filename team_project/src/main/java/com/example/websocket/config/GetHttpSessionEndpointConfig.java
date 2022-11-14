package com.example.websocket.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

public class GetHttpSessionEndpointConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Object httpSession = request.getHttpSession();
        Map<String, Object> properties = sec.getUserProperties();
        properties.put(httpSession.getClass().getName(),httpSession);
    }
}
