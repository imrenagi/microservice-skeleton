package com.imrenagi.service_auth.repository;

import com.imrenagi.service_auth.domain.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by imrenagi on 5/13/17.
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

}
