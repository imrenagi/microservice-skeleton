package com.imrenagi.service_auth.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by imrenagi on 5/12/17.
 */
@Entity
@Table(name="permissions")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

//    @ManyToMany(mappedBy = "privileges")
//    private Collection<Role> roles;

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

//    public Collection<Role> getRoles() {
//        return roles;
//    }

//    public void setRoles(Collection<Role> roles) {
//        this.roles = roles;
//    }
}
