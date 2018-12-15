package com.seu.architecture.service.impl;

import com.seu.architecture.model.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolderServiceImpl {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
