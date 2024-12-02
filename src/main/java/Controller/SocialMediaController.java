package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.util.List;

public class SocialMediaController {
// chatgpt, suu, and I created all of this. feel free to delete or edit//
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

        app.post("/register", ctx -> {
            try {
                Account account = objectMapper.readValue(ctx.body(), Account.class);
                Account newAccount = accountService.registerAccount(account);
                if (newAccount != null) {
                    ctx.json(newAccount).status(200);
                } else {
                    ctx.result("").status(400);
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        app.post("/login", ctx -> {
            try {
                Account credentials = objectMapper.readValue(ctx.body(), Account.class);
                Account account = accountService.login(credentials.getUsername(), credentials.getPassword());
                if (account != null) {
                    ctx.json(account).status(200);
                } else {
                    ctx.result("").status(401);
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        app.post("/messages", ctx -> {
            try {
                Message message = objectMapper.readValue(ctx.body(), Message.class);
                Message newMessage = messageService.createMessage(message);
                if (newMessage != null) {
                    ctx.json(newMessage).status(200);
                } else {
                    ctx.result("").status(400);
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        app.get("/messages", ctx -> {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages).status(200);
        });

        app.get("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message message = messageService.getMessageById(message_id);
                if (message != null) {
                    ctx.json(message).status(200);
                } else {
                    ctx.result("").status(404);
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

        app.delete("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message deletedMessage = messageService.deleteMessageById(message_id);
                if (deletedMessage != null) {
                    ctx.status(200).json(deletedMessage);
                } else {
                    ctx.status(404).result("Message not found");
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid message ID");
            }
        });

        app.patch("/messages/{message_id}", ctx -> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message updatedMessageInput = objectMapper.readValue(ctx.body(), Message.class);

                Message updatedMessage = messageService.updateMessageText(message_id, updatedMessageInput.getMessage_text());
                if (updatedMessage != null) {
                    ctx.json(updatedMessage).status(200);
                } else {
                    ctx.result("").status(400);
                }
            } catch (Exception e) {
                ctx.result("").status(400);
            }
        });

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
