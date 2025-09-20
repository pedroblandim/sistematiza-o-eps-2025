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

    public User() {}

    public User(UUID id, String email, String password, boolean isAdmin, Date createDate, Date modifiedDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }


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
