package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) {
        // Validate message text length
        if (message.getMessage_text() == null || 
            message.getMessage_text().isEmpty() || 
            message.getMessage_text().length() > 255) {
            return null;
        }

        // Validate that the user exists
        Account user = accountDAO.getAccountById(message.getPosted_by());
        if (user == null) {
            return null;
        }

        try {
            // Set current time if not provided
            if (message.getTime_posted_epoch() == 0) {
                message.setTime_posted_epoch(System.currentTimeMillis() / 1000);
            }
            return messageDAO.createMessage(message);
        } catch (Exception e) {
            System.err.println("Error creating message: " + e.getMessage());
            return null;
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