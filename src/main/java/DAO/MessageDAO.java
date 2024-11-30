package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?) RETURNING message_id";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                message.setMessage_id(rs.getInt("message_id"));
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        String sql = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, message_id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message deleteMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectQuery = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                Message messageToDelete = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );

                String deleteQuery = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();

                return messageToDelete;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMessageText(int message_id, String newMessageText) {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newMessageText);
            pstmt.setInt(2, message_id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
