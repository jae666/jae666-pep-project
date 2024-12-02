package Controller;

import Model.Account;
import Model.Message; 
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService(); // accountService = new instance of AccountService
        this.messageService = new MessageService(); // Initialize MessageService
    }

    private void registerHandler(Context ctx) throws Exception {
        Account account = ctx.bodyAsClass(Account.class);
        Account newAccount = accountService.addAccount(account);

        if (newAccount != null) {
            ctx.status(200).json(newAccount);
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class); // Get login credentials from request body
    
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

    // POST handler to create a new message
    private void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
    
        // Validate message text: It should not be blank or exceed 255 characters
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            ctx.status(400).result("Message text must not be blank and must be less than 255 characters.");
            return;
        }
    
        // Validate if the user exists
        Account user = accountService.getAccountById(message.getPosted_by());
        if (user == null) {
            ctx.status(400).result("User does not exist.");
            return;
        }
    
        // Set the current timestamp for the message
        message.setTime_posted_epoch(System.currentTimeMillis()); // Directly set the field
    
        // Create and save the message
        Message createdMessage = messageService.createMessage(message);
    
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400).result("Failed to create message.");
        }
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler); // New endpoint for login
        app.post("/messages", this::createMessageHandler); // Endpoint for creating a message
        return app;
    }
}
