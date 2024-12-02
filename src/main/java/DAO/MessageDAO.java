package DAO;

import Model.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;  // Import ResultSet
import java.sql.SQLException;

public class MessageDAO {

    // Declare the connection object
    private Connection connection;

    // Constructor to initialize the connection
    public MessageDAO() {
        try {
            // Assuming you are using MySQL, update the connection details accordingly
            String url = "jdbc:mysql://localhost:3306/your_database";
            String username = "your_username";
            String password = "your_password";

            // Create the connection to the database
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert a new message into the database
    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());
    
            // Execute the insert statement
            int rowsAffected = statement.executeUpdate();
            
            // If the insert was successful, retrieve the generated message ID
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        message.setMessage_id(generatedKeys.getInt(1)); // Set the generated ID to the message
                    }
                }
            }
            return message; // Return the message with the generated ID
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle exception (you might want to throw a custom exception instead)
        }
    }
}
