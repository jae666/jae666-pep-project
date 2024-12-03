package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection(); 
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                System.out.println("Account id: " + rs.getInt("account_id"));
                System.out.println("Account username: " + rs.getString("username"));
                System.out.println("Account password: " + rs.getString("password")); 
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Account (account_id, username, password) VALUES (?,?,?) ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1,account.getAccount_id());
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.setString(3,account.getPassword());

            preparedStatement.executeUpdate();
            return account; 
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; 
    }

    public Object getUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
                   return account ;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account create(Account account) throws Exception {
        Connection connection =  ConnectionUtil.getConnection();
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                //account.setAccount_id(generatedKeys.getInt(1));
                Account acc = new Account(generatedKeys.getInt(1), account.getUsername(), account.getPassword());
                return acc;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    public Account getAccountByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Return the account if found
                return new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

        // Method to retrieve an account by its ID
        public Account getAccountById(int accountId) {
            Connection connection = ConnectionUtil.getConnection();
            String query = "SELECT * FROM account WHERE account_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, accountId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;  
        }
    }