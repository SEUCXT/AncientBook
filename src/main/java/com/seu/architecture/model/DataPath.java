package com.seu.architecture.model;

import javax.persistence.*;

/**
 * Created by 17858 on 2017-11-20.
 */
@Entity
@Table(name = "data_path")
public class DataPath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "type", unique = true, nullable = false)
    String type;

    @Column(name = "path", nullable = false)
    String path;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
