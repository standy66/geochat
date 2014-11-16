package me.standy.geochat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrew on 16.11.14.
 */
public class ChatMessage {
    public String messageBody;
    public String sender;
    //TODO: maybe we do not need isMine at all
    public boolean isMine;

    public ChatMessage(String sender, String messageBody, boolean isMine) {
        this.sender = sender;
        this.messageBody = messageBody;
        this.isMine = isMine;
    }

    public static final ChatMessage fromJSON(JSONObject jsonObject) {
        if (jsonObject == null)
            throw new IllegalArgumentException("jsonObject is null");

        try {
            String sender = jsonObject.getString("sender");
            String messageBody = jsonObject.getString("message");
            //we guaranteed that sender and messageBody is not null here
            return new ChatMessage(sender, messageBody, false);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", sender);
            jsonObject.put("message", messageBody);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }
}
