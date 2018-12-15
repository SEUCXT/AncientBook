package com.seu.architecture.repository;

import com.seu.architecture.model.TaskInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by 17858 on 2017-09-29.
 */
@Repository
public interface TaskRepository extends CrudRepository<TaskInfo, Long> {

    TaskInfo findByJobName(String jobName);

}
