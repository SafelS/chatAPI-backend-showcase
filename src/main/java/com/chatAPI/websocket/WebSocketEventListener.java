package com.chatAPI.websocket;

import com.chatAPI.enums.Status;
import com.chatAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserService userService;

    @EventListener
    public void handleConnect(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();
        userService.updateStatus(username, Status.ONLINE);
    }

    @EventListener
    public void handleDisconnect(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();
        userService.updateStatus(username, Status.OFFLINE);
    }

}
