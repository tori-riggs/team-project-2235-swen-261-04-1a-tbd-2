package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.model.Message;
import com.ufund.api.ufundapi.service.AuthService;
import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("message")
public class MessageController {
    MessageService messageService;
    AuthService authService;
    private static final Logger LOG = Logger.getLogger(MessageController.class.getName());

    public MessageController(MessageService messageService, AuthService authService) {
        this.messageService = messageService;
        this.authService = authService;
    }

    @GetMapping("")
    public ResponseEntity<Message[]> getAllMessages(@RequestParam String username, @RequestParam String password) {
        LOG.info("GET /message/");
        try {
            if(authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)) {
                Message[] messages = messageService.getAllMessages();
                return new ResponseEntity<>(messages, HttpStatus.OK);
            }
            else if(authService.hasPermissionLevel(username, password, AuthLevel.USER)){
                Message[] messages = messageService.getAllMessages();
                return new ResponseEntity<>(messages, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Message[]> getMessagesFromUser(@RequestParam String username, @RequestParam String password, @PathVariable String senderUsername) {
        LOG.info("GET /message/");
        try {
            /** 
            if(!authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            */
            Message[] messages = messageService.getMessagesByUsername(senderUsername);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Message> createMessage(@RequestParam String username, @RequestParam String password, @RequestBody Message message) {
        LOG.info("POST /message/");
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            System.out.println(message);
            Message newMessage = messageService.createMessage(message);
            return new ResponseEntity<>(newMessage, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Message> deleteMessage(@RequestParam String username, @RequestParam String password, @RequestParam int id) {
        LOG.info("DELETE /message/");
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            messageService.deleteMessage(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
