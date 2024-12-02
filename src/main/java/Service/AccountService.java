package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
// chatgpt, suu, and I created all of this. feel free to delete or edit//
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() <= 4) {
            return null;
        }
        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }
}
