package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Method to create a new message
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null; // Message text must not be blank and must be under 255 characters
        }
        return messageDAO.createMessage(message);
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Method to retrieve a message by ID
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    // Method to delete a message by ID
    public boolean deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    // Method to update a message's text
    public boolean updateMessageText(int message_id, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return false; // Message text must not be blank and must be under 255 characters
        }
        return messageDAO.updateMessageText(message_id, newMessageText);
    }

    // Method to retrieve all messages by a specific user
    public List<Message> getMessagesByUser(int account_id) {
        return messageDAO.getAllMessages().stream()
                .filter(message -> message.getPosted_by() == account_id)
                .toList();
    }
}
