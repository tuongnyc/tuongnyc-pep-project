package Service;

import java.util.*;

import DAO.MessageDAO;
import Model.Message;

/*
 * The service class for message
 */
public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    /*
     * addMessage will take a message object and insert into the database.  If unsuccessful, returns null.
     */
    public Message addMessage(Message message){
        String msg = message.getMessage_text().trim();

        if (msg.length() > 0 && msg.length() < 255) {
            return messageDAO.insertMessage(message);
        }

        return null;
    }

    /*
     * getAllMessages return a list of all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /*
     * getAllMessagesByAccountId return a list of all messages based on the posted id.
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        return messageDAO.getAllMessagesByAccountId(id);
    }

    /*
     * getMessageById return a message based on the message id.  Returns null if not found.
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /*
     * deleteMessageById deletes a message by message id.  If successfull, return the message.
     * Otherwise returns null.
     */
    public Message deleteMessageById(int id) {
        Message message = messageDAO.getMessageById(id);  // save the message to return back to user.

        return (messageDAO.deleteMessageById(id) > 0) ? message : null;
    }

    /*
     * updateMessageById update the message by the message id with the new msg_txt.  If successful,
     * return the new message with the updated text.  Otherwise, returns null.
     */
    public Message updateMessageById(int id, String msg_text) {

        Message message = messageDAO.getMessageById(id);

        if (msg_text.length() > 0 && msg_text.length() < 255 && message != null) {
            message.setMessage_text(msg_text);

            int rowsAffected = messageDAO.updateMessageById(id, msg_text);
            return (rowsAffected > 0) ? message : null;
        }
        return null;
    }
}