package com.v1.gcm.gcmv1.domain;

/**
 * Created by cesar on 21/01/16.
 */
public class PushMessage {
    private String title;
    private String message;

    public PushMessage() {}
    public PushMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
