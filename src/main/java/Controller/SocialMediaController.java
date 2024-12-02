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
    public SocialMediaController(){
        this.accountService = new AccountService();
        // accountService = new instance of AccountService in AccountService.java//
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
        Account credentials = ctx.bodyAsClass(Account.class);
        Account authenticatedAccount = accountService.validateLogin(credentials.getUsername(), credentials.getPassword());
    
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);
        } else {
            ctx.status(401);
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

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}