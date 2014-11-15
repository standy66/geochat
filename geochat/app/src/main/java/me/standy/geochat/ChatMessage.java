package me.standy.geochat;

/**
 * Created by andrew on 16.11.14.
 */
public class ChatMessage {
    public String message;
    public String author;
    public boolean isMine;

    public ChatMessage(String author, String message, boolean isMine) {
        this.author = author;
        this.message = message;
        this.isMine = isMine;
    }
}
