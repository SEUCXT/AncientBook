package com.seu.architecture.service.impl;

import com.seu.architecture.model.LoginTicket;
import com.seu.architecture.model.User;
import com.seu.architecture.repository.LoginTicketRepository;
import com.seu.architecture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class LoginTicketService {

    @Autowired
    LoginTicketRepository loginTicketRepository;

    @Autowired
    UserRepository userRepository;

    public String createLoginTicket(String username) {
        User user = userRepository.findByUsername(username);
        LoginTicket ticket = new LoginTicket();
        ticket.setUser(user);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketRepository.save(ticket);
        return ticket.getTicket();
    }
}
