package com.seu.architecture.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    Set<User> user;

    @Column(name = "name", unique = true, nullable = false)
    String name;

    String displayName;

    String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "role_permission", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "permission_id",
                    nullable = false, updatable = false)})
    Set<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
