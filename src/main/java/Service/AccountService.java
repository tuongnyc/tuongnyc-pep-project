package Service;

import java.util.*;

import DAO.AccountDAO;
import Model.Account;

/*
 * The service class.
 */
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /*
     * getAllAccounts return a list of accounts from the database.
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /*
     * addAccount will return the account that was added to the database.  If unsuccessful, then return null.
     */
    public Account addAccount(Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        Account existUser = accountDAO.getAccountByUserName(username); 
    
        if (username.length() > 0 && existUser == null && password.length() > 4) {
            Account newAcc = accountDAO.insertAccount(account);
            return newAcc;
        }
            
        return null;
    }

    /*
     * getAccountByUsername return an account based on username, if not exist then return null.
     */
    public Account getAccountByUsername(String username) {

        if (username.trim().length() > 0) {
            return accountDAO.getAccountByUserName(username);
        }
        return null;   
    }

}