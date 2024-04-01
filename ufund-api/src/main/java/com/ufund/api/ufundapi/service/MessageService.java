package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.model.Message;
import com.ufund.api.ufundapi.persistence.MessageDAO;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MessageService {

    MessageDAO messageDAO;
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message[] getAllMessages() throws IOException {
        return messageDAO.getAllMessages();
    }

    public Message[] getMessagesByUsername(String username) throws IOException {
        return messageDAO.getMessagesByUsername(username);
    }

    public Message createMessage(Message message) throws IOException {
        return messageDAO.createMessage(message);
    }

    public boolean deleteMessage(int id) throws IOException {
        return messageDAO.deleteMessage(id);
    }
}
