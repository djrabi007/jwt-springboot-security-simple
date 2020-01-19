package com.javatechie.jwt.api.service;

import com.javatechie.jwt.api.entity.User;
import com.javatechie.jwt.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService 
				implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    /**
     * Case-1 
     * 1. client hit "/authenticate"
     * 2. Request(Username and Password) vs Database (username and Password)
     * 3. Populate UserDetails(Spring Security) Object 
     * 
     * Case-2
     * 4. client hit "/" ,"userDetails"
     * 5. Do #3 
     */
    @Override
    public UserDetails loadUserByUsername(String name) 
    		throws UsernameNotFoundException {
        //User user = repository.findByUserName(username);
        User user = repository.findByName(name);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
    }
}
