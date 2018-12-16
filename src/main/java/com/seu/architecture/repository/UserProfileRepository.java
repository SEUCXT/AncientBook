package com.seu.architecture.repository;


import com.seu.architecture.model.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by 17858 on 2018-12-05.
 */
@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    UserProfile findByUsername(String username);
}
