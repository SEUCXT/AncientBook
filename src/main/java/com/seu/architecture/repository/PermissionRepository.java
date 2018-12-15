package com.seu.architecture.repository;

import com.seu.architecture.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

    Permission findByname(String name);

}
