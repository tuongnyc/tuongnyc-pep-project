package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Util.ConnectionUtil;
import Model.Message;

public class MessageDAO {

    /*
     * getAllMessages will return a list of all messages in the database
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /*
     * getAllMessagesByAccountId returns a list of all messages based on the message id.
     */
    public List<Message> getAllMessagesByAccountId(int id){
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /*
     * getMessageById return a message based on the message id.
     */
    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /*
     * insertMessage returns a message that was inserted successfully into the message table.
     */
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try  {

            // creating epoch time for message log.
            //Instant instant = Instant.now();
            //Timestamp timestamp = Timestamp.from(instant);

            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?);" ;

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            //preparedStatement.setLong(3, timestamp.getTime());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getInt(1);
                return new Message(generated_message_id, 
                                   message.getPosted_by(), 
                                   message.getMessage_text(),
                                   message.getTime_posted_epoch());
            }
            return null;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * deleteMessageById delete a message from the database based on the message id and return
     * the number of rows that were deleted.
     */
    public int deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString method here
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /*
     * updateMessageById will update the message table based on the message id with the new message.
     * It will returns the number of rows affected with the update.
     */
    public int updateMessageById(int id, String msg) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString method here
            preparedStatement.setString(1, msg);
            preparedStatement.setInt(2,id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
