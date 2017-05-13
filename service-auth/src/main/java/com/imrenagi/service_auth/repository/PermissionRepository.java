package com.imrenagi.service_auth.repository;

import com.imrenagi.service_auth.domain.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by imrenagi on 5/12/17.
 */
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Permission findByName(String name);
}
