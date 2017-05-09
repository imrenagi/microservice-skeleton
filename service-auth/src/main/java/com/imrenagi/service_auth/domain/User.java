package com.imrenagi.service_auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Date;

/**
 * Created by imrenagi on 5/8/17.
 */
@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    private String username;

    private String password;

//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @Column(name = "updated_at")
//    private Date updatedAt;
//
//    @Column(name = "deleted_at")
//    private Date deletedAt;

    protected User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.username, this.password);
    }
}
