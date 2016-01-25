package com.v1.gcm.gcmv1.domain;

/**
 * Created by cesar on 21/01/16.
 */
public class WrapObjToNetwork {

    private User user;
    private String method;

    public WrapObjToNetwork(User user, String method) {
        this.user = user;
        this.method = method;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }



}
