package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import DAO.AccountDAO;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

//test//
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private AccountDAO accountDAO;

    public SocialMediaController(){
        this.accountService = new AccountService();// accountService = new instance of AccountService in AccountService.java//
        this.accountDAO = new AccountDAO(); // Initialize the accountDAO
    }

    private void registerHandler(Context ctx) throws Exception {
        Account account = ctx.bodyAsClass(Account.class);
        Account newAccount = accountService.addAccount(account);

        //check if account is already added
       
            //Account newAccount = accountService.addAccount(account);
            if(newAccount != null) {
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

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler); // New endpoint for login
        return app;
    }
}