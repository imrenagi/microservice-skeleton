package com.imrenagi.service_auth.repository;

import com.imrenagi.service_auth.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by imrenagi on 5/8/17.
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
