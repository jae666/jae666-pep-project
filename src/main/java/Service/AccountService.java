package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Method to register a new account
    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null; // Username must not be blank
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            return null; // Password must be over 4 characters
        }

        // Check if the username is already taken
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null; // Username already exists
        }

        return accountDAO.createAccount(account);
    }

    // Method to login
    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account; // Login successful
        }
        return null; // Invalid username or password
    }
}
