package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Message;

public interface MessageDAO {
    Message[] getMessagesByUsername(String username) throws IOException;
    Message[] getAllMessages() throws IOException;
    boolean deleteMessage(int id) throws IOException;
    Message createMessage(Message message) throws IOException;
}
