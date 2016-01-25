package com.v1.gcm.gcmv1.domain;

/**
 * Created by cesar on 21/01/16.
 */
public class User {

    private String registrationId;

    public User() {}
    public User(String registrationId) {
        this.registrationId = registrationId;
    }
    public String getRegistrationId() {
        return registrationId;
    }
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

}
