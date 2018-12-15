package com.seu.architecture.repository;

import com.seu.architecture.model.LoginTicket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginTicketRepository extends CrudRepository<LoginTicket, Long> {

    LoginTicket findByTicket(String ticket);
}
