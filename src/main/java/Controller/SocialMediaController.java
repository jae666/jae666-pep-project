package Controller;

import Model.Account;
import Model.Message; 
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    // Make handlers public and implement Handler interface
    public void registerHandler(Context ctx) throws Exception {
        Account account = ctx.bodyAsClass(Account.class);
        Account newAccount = accountService.addAccount(account);

        if (newAccount != null) {
            ctx.status(200).json(newAccount);
        } else {
            ctx.status(400);
        }
    }

    public void loginHandler(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
    
        // Validate input
        if (credentials.getUsername() == null || credentials.getUsername().isEmpty() ||
            credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            ctx.status(400).json("Username and password must not be empty.");
            return;
        }
    
        // Attempt to validate login (username and password)
        Account authenticatedAccount = accountService.validateLogin(credentials.getUsername(), credentials.getPassword());
    
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);  // Return the account including account_id if login is successful
        } else {
            // For invalid username or password, return 401 with empty body
            ctx.status(401).result("");  // Empty response body as per test case expectations
        }
    }

    public void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
    
        // Validate message text: Cannot be blank or over 255 characters
        if (message.getMessage_text() == null || 
            message.getMessage_text().isEmpty() || 
            message.getMessage_text().length() > 255) {
            ctx.status(400);
            ctx.result("");  // Explicitly set empty result
            return;
        }
    
        // Validate if the user exists
        Account user = accountService.getAccountById(message.getPosted_by());
        if (user == null) {
            ctx.status(400);
            ctx.result("");  // Explicitly set empty result
            return;
        }
    
        // Create and save the message
        Message createdMessage = messageService.createMessage(message);
    
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400);
            ctx.result("");  // Explicitly set empty result
        }
    }

    // Method to retrieve all messages
    public void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    // Method to retrieve a message by ID
    public void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200); // As per requirement, empty body if no message found
        }
    }

    // Mthod to delete a message
    public void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        
        if (deletedMessage != null) {
            ctx.status(200).json(deletedMessage);
        } else {
            ctx.status(200); // Idempotent response
        }
    }

    // Method to update a message text
    public void updateMessageTextHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message incomingMessage = ctx.bodyAsClass(Message.class);
        String newText = incomingMessage.getMessage_text();

        Message updatedMessage = messageService.updateMessageText(messageId, newText);
        
        if (updatedMessage != null) {
            ctx.status(200).json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    // Mthod to retrieve messages by account ID
    public void getMessagesByAccountIdHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        
        ctx.status(200).json(messages);
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // Existing endpoints
        app.post("/register", ctx -> registerHandler(ctx));
        app.post("/login", ctx -> loginHandler(ctx));
        app.post("/messages", ctx -> createMessageHandler(ctx));
        app.get("/messages", ctx -> getAllMessagesHandler(ctx));
        app.get("/messages/{message_id}", ctx -> getMessageByIdHandler(ctx));
        app.delete("/messages/{message_id}", ctx -> deleteMessageHandler(ctx));
        app.patch("/messages/{message_id}", ctx -> updateMessageTextHandler(ctx));
        app.get("/accounts/{account_id}/messages", ctx -> getMessagesByAccountIdHandler(ctx));
        
        return app;
    }
}