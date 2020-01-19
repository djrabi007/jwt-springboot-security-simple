package com.javatechie.jwt.api;

import com.javatechie.jwt.api.entity.User;
import com.javatechie.jwt.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringSecurityJwtExampleApplication {
    @Autowired
    private UserRepository repository;

    /**
     * Insert/Load Data into H2 Database 
     */
    @PostConstruct
    public void initUsers() {
        List<User> users = Stream.of(
                new User(101, "Rabi", "p", "djrabi@gmail.com"),
                new User(102, "Roddur", "pwd1", "user1@gmail.com"),
                new User(103, "Baba", "pwd2", "user2@gmail.com"),
                new User(104, "Ma", "pwd3", "user3@gmail.com")
        ).collect(Collectors.toList());
        repository.saveAll(users);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtExampleApplication.class, args);
    }

}
