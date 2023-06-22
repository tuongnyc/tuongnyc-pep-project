package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postMessageHandler);

        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);

        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        
        return app;
    }

    /**
     * getMessagesHandler for processing getting the messages from the database
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /*
     * getMessageByIdHandler obtains the id from the URL input and return the list of messages as a response.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(msg_id);
        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.json("");
        }
    }

    /*
     * getMessagesByAccountHandler obtains account id from the get parameter and return a list as a response.
     */
    private void getMessagesByAccountHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getAllMessagesByAccountId(account_id);
        ctx.json(messages);
    }

    /*
     * postAccountHandler obtains the account information from the post body and create/insert
     * the user account into account database.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);

        account.setUsername(account.getUsername().trim());
        account.setPassword(account.getPassword().trim());

        Account addedAccount = accountService.addAccount(account);
        if(addedAccount == null) {
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    /*
     * postAccountLoginHandler obtains the user information from the body and check to see if the user is valid
     * If the user is not valid return a response status of 401.
     */
    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);
        account.setUsername(account.getUsername().trim());
        account.setPassword(account.getPassword().trim());

        Account existingAccount = accountService.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            ctx.json(mapper.writeValueAsString(existingAccount));
        }
        else {
            ctx.status(401);
        }
    }

    /*
     * postMessageHandler obtains the message from the body and insert/add the message to the message table.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(ctx.body(), Message.class);
        message.setMessage_text(message.getMessage_text().trim());

        Message insertedMessage = messageService.addMessage(message);
        if (insertedMessage != null) {
            ctx.json(mapper.writeValueAsString(insertedMessage));
        }
        else {
            ctx.status(400);
        }
    }

    /*
     * deleteMessageByIdHandler obtains the message id, if the message doesn't exist, response back with an empty body.
     * otherwise reponse with the message that was deleted.
     */
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message message = messageService.deleteMessageById(msg_id);
        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.result("");  // empty body.
        }
    }

    /*
     * updateMessageByIdHandler obtains the message id from the parameter.  If the message exist,
     * then updated with the new message from the body.  Otherwise return a 400 status.
     * 
     * Use the map object to parse the JSON object.
     */
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));
        // get the body
        String body = ctx.body();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(body, Map.class);
        String value = map.get("message_text");

        Message message = messageService.updateMessageById(msg_id, value.trim());

        if (message != null) {
            ctx.json(message);
        }
        else {
            ctx.status(400);
        }

    }
}