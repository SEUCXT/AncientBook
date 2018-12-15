package com.seu.architecture.interceptor;

import com.seu.architecture.model.LoginTicket;
import com.seu.architecture.model.User;
import com.seu.architecture.repository.LoginTicketRepository;
import com.seu.architecture.repository.UserRepository;
import com.seu.architecture.service.impl.HostHolderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginTicketRepository loginTicketRepository;

    @Autowired
    private HostHolderServiceImpl hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        ticket = httpServletRequest.getHeader("ticket");
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketRepository.findByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }
            User user = loginTicket.getUser();
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
