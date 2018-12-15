package com.seu.architecture.repository;

import com.seu.architecture.model.DataPath;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by 17858 on 2017-11-20.
 */
public interface DataPathRepository extends CrudRepository<DataPath, Long> {

    DataPath findByType(String type);

}
