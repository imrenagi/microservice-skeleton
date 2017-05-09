package com.imrenagi.service_account.domains;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by imrenagi on 5/8/17.
 */
public class User {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
