package com.imrenagi.service_auth.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by imrenagi on 5/12/17.
 */
@Entity
@Table(name="roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

//    //TODO remove this!!
//    @ManyToMany(mappedBy = "roles")
//    private Collection<User> users;
//
//    @JoinTable(name = "role_permissions",
//            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
//    @ManyToMany
//    private Collection<Permission> privileges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Collection<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Collection<User> users) {
//        this.users = users;
//    }
//
//    public Collection<Permission> getPrivileges() {
//        return privileges;
//    }
//
//    public void setPrivileges(Collection<Permission> privileges) {
//        this.privileges = privileges;
//    }
}
