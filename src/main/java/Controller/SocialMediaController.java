package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.util.List;

public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountService = new AccountService(new DAO.AccountDAO());
        this.messageService = new MessageService(new DAO.MessageDAO());
        this.objectMapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Endpoint: Register a new account
        app.post("/register", ctx -> {
            try {
                Account account = objectMapper.readValue(ctx.body(), Account.class);
                Account newAccount = accountService.registerAccount(account);
                if (newAccount != null) {
                    ctx.json(newAccount).status(200);
                } else {
                    ctx.result("").status(400); // Invalid input or duplicate username
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        // Endpoint: Login
        app.post("/login", ctx -> {
            try {
                Account credentials = objectMapper.readValue(ctx.body(), Account.class);
                Account account = accountService.login(credentials.getUsername(), credentials.getPassword());
                if (account != null) {
                    ctx.json(account).status(200);
                } else {
                    ctx.result("").status(401); // Unauthorized
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        // Endpoint: Create a new message
        app.post("/messages", ctx -> {
            try {
                Message message = objectMapper.readValue(ctx.body(), Message.class);
                Message newMessage = messageService.createMessage(message);
                if (newMessage != null) {
                    ctx.json(newMessage).status(200);
                } else {
                    ctx.result("").status(400); // Invalid message
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        // Endpoint: Retrieve all messages
        app.get("/messages", ctx -> {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages).status(200);
        });

        // Endpoint: Retrieve a message by ID
        app.get("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message message = messageService.getMessageById(message_id);
                if (message != null) {
                    ctx.json(message).status(200);
                } else {
                    ctx.result("").status(404); // Not found
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        // Endpoint: Delete a message by ID
        app.delete("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message deletedMessage = messageService.deleteMessageById(message_id);
                if (deletedMessage != null) {
                    ctx.status(200).json(deletedMessage); // Return the deleted message
                } else {
                    ctx.status(404).result("Message not found"); // Message not found
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid message ID"); // Invalid input format
            }
        });

        // Endpoint: Update a message's text
        app.patch("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message updatedMessage = objectMapper.readValue(ctx.body(), Message.class);
                boolean updated = messageService.updateMessageText(message_id, updatedMessage.getMessage_text());
                if (updated) {
                    ctx.result("").status(200);
                } else {
                    ctx.result("").status(400); // Invalid input or not found
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        // Endpoint: Retrieve all messages by a specific user
        app.get("/accounts/{account_id}/messages", ctx -> {
            try {
                int account_id = Integer.parseInt(ctx.pathParam("account_id"));
                List<Message> messages = messageService.getMessagesByUser(account_id);
                ctx.json(messages).status(200);
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        return app;
    }
}
