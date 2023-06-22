package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {
    /*
     * @return all accounts from the account database
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();

        List<Account> accounts = new ArrayList<>();

        try {
            //Write SQL logic here
            String sql = "SELECT * FROM Account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                rs.getString("username"),
                rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /*
     * insertAccount will insert an account into the database.  The original account info doesn't have account_id.  
     * The database will generate the key for us.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try  {

            String sql = "INSERT INTO account(username, password) VALUES(?,?);" ;

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
            return null;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * getAccountByUserName will return the account based on the username
     */
    public Account getAccountByUserName(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM Account WHERE username = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            // assuming there are no two account the same.
            Account account = null;
            if (rs.next()){
                account = new Account(rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password"));
            }
            return account;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
