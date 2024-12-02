package Service;

import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO(); // Initialize MessageDAO
    }

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message); // Persist the message and return the created message
    }
}
