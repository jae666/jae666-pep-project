package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    // Constructor//
    public AccountService(){

        this.accountDAO = new AccountDAO();

    }

// Requirements for Account Creation: Not Blank, atleast 4 characters long, cannot exists in the database already//
// Method// 
    public Account addAccount(Account account) throws Exception{
        if(account.getUsername().trim().isEmpty() || account.getUsername() == null){
            return null;
        }
        
        if(account.getPassword().length() < 4 || account.getPassword() == null){
            return null;
        }
            
        return accountDAO.create(account);
    }

    public List <Account> getAllAccounts()  {
        return accountDAO.getAllAccounts();
    }

    public Account validateLogin(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;  
        }
    
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }
}
