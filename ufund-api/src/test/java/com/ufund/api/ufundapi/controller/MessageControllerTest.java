package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.model.Message;
import com.ufund.api.ufundapi.service.AuthService;
import com.ufund.api.ufundapi.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("Controller-tier")
public class MessageControllerTest {
    private MessageController messageController;
    private MessageService messageService;
    private AuthService authService;

    @BeforeEach
    public void setupAuthController() {
        messageService = mock(MessageService.class);
        authService = mock(AuthService.class);
        messageController = new MessageController(messageService, authService);
    }

    @Test
    public void whenGetAllMessages_thenReturn200() throws IOException {
        //given
        String username = "username";
        String password = "password";

        when(authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)).thenReturn(true);

        //when
        ResponseEntity response = messageController.getAllMessages(username, password);

        //then
        verify(messageService, times(1)).getAllMessages();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetAllMessages_withoutPermission_thenThrowForbidden() throws IOException {
        //given
        String username = "username";
        String password = "password";

        when(authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)).thenReturn(false);

        //when
        ResponseEntity response = messageController.getAllMessages(username, password);

        //then
        verify(messageService, never()).getAllMessages();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void whenGetMessagesByUsername_thenReturn200() throws IOException {
        //given
        String username = "username";
        String password = "password";
        String sender = "sender";

        when(authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)).thenReturn(true);

        //when
        ResponseEntity response = messageController.getMessagesFromUser(username, password, sender);

        //then
        verify(messageService, times(1)).getMessagesByUsername(sender);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetMessagesFromUser_withoutPermission_thenThrowForbidden() throws IOException {
        //given
        String username = "username";
        String password = "password";
        String sender = "sender";

        when(authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)).thenReturn(false);

        //when
        ResponseEntity response = messageController.getMessagesFromUser(username, password, sender);

        //then
        verify(messageService, never()).getMessagesByUsername(sender);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void whenCreateMessage_thenReturn200() throws IOException {
        //given
        String username = "username";
        String password = "password";
        String text = "message";
        Message message = new Message(0, username, "0/0/0", text);


        when(authService.hasPermissionLevel(username, password, AuthLevel.USER)).thenReturn(true);

        //when
        ResponseEntity response = messageController.createMessage(username, password, message);

        //then
        verify(messageService, times(1)).createMessage(message);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenCreateMessage_withoutPermission_thenThrowForbidden() throws IOException {
        //given
        String username = "username";
        String password = "password";
        String text = "message";
        Message message = new Message(0, username, "0/0/0", text);

        when(authService.hasPermissionLevel(username, password, AuthLevel.USER)).thenReturn(false);

        //when
        ResponseEntity response = messageController.createMessage(username, password, message);

        //then
        verify(messageService, never()).createMessage(message);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void whenDeleteMessage_thenReturn200() throws IOException {
        //given
        String username = "username";
        String password = "password";
        int id = 0;


        when(authService.hasPermissionLevel(username, password, AuthLevel.USER)).thenReturn(true);

        //when
        ResponseEntity response = messageController.deleteMessage(username, password, id);

        //then
        verify(messageService, times(1)).deleteMessage(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenDeleteMessage_withoutPermission_thenThrowForbidden() throws IOException {
        //given
        String username = "username";
        String password = "password";
        int id = 0;

        when(authService.hasPermissionLevel(username, password, AuthLevel.USER)).thenReturn(false);

        //when
        ResponseEntity response = messageController.deleteMessage(username, password, id);

        //then
        verify(messageService, never()).deleteMessage(id);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
