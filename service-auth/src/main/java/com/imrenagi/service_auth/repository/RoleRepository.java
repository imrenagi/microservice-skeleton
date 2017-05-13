package com.imrenagi.service_auth.repository;

import com.imrenagi.service_auth.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by imrenagi on 5/12/17.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    //TODO need add index in name column
    Role findByName(String name);
}
