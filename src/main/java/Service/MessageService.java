package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
// chatgpt, suu, and I created all of this. feel free to delete or edit//
    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessageText(int message_id, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        boolean updated = messageDAO.updateMessageText(message_id, newMessageText);
        if (updated) {
            return messageDAO.getMessageById(message_id);
        }
        return null;
    }

    public List<Message> getMessagesByUser(int account_id) {
        return messageDAO.getAllMessagesByUser(account_id);
    }
}
