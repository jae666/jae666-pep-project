package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
    // Additional validation can be added here if needed
    if (message.getMessage_text() == null || 
        message.getMessage_text().isEmpty() || 
        message.getMessage_text().length() > 255) {
        return null;
    }

    try {
        return messageDAO.createMessage(message);
    } catch (Exception e) {
        System.err.println("Error creating message: " + e.getMessage());
            return null; // Or throw a custom exception
    }
}

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessageText(int messageId, String newText) {
        // Additional validation can be added here if needed
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}