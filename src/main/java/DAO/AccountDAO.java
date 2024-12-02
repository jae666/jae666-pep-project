package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // chatgpt, suu, and I created all of this. feel free to delete or edit//
    public void createAccount(Account account)  throws IOException, InterruptedException {
       if (account.getUsername().equals("") || account.getPassword().length() <= 3){ 
            return;
        } else {
            String sql = "SELECT * FROM account WHERE username = '?'";
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, account.getUsername());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            String userN = account.getUsername();
            String userPW = account.getPassword();

            HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"" + userN + "\", \"password\": \"" + userPW + "\" }"))
                .header("Content-Type", "application/json")
                .build();
            HttpClient webClient = HttpClient.newHttpClient();
            int res = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString()).statusCode();
            System.out.println(res);

            /*sql = "INSERT INTO account (username, password) VALUES (?, ?) RETURNING account_id";
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, account.getUsername());
                pstmt.setString(2, account.getPassword());
    
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    account.setAccount_id(rs.getInt("account_id"));
                    return account;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }*/

        }
    }

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
