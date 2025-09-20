package br.com.simplifytec.gamification.users.domain.model;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;

    private String password;

    private boolean isAdmin;

    private Date createDate;
    private Date modifiedDate;


    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }
}
