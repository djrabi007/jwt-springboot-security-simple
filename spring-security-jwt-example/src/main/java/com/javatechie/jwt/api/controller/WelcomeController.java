package com.javatechie.jwt.api.controller;

import com.javatechie.jwt.api.entity.AuthRequest;
import com.javatechie.jwt.api.entity.User;
import com.javatechie.jwt.api.repository.UserRepository;
import com.javatechie.jwt.api.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin
public class WelcomeController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;
    /**
     * ***** No Header(Authorization) ...***
     * 1. XXService.java- Request(Username and Password)
     *     vs Database (username and Password)
     * 2. XXUtil.java- Generate Token using "username" only 
     *   after #1 successful (by XXutil)
     * @param authRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
    //public ResponseEntity<?>  generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                    (authRequest.getUserName(), 
                    		authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
       return jwtUtil.generateToken(authRequest.getUserName());
        //return ResponseEntity.ok(jwtUtil.generateToken(authRequest.getUserName()));
    }
    
    /**
     * ****Checking TOKEN only....NOT Password*********
     * *****Password retrieved from Database and populate Security Object******
     * 1. Accessible using Header(Authorization) ..
     * 2. XXFilter.java- It will Go to XXFilter i.e JwtFilter.java for Token Validation
     * 		2A. Compare username(Token) vs  username(H2 Database)
     * 		2B. Token Expred ? 
     * 3. Then only Give resource
     * @return
     */
    @GetMapping("/")
    public String welcome() {
        return "Welcome to javatechie !!";
    }
    
    /* 
     * Same as "/" 
     * 
     */
    @GetMapping("/allUsers")
	public List<User> getUser() {
		return repository.findAll();
	}

    
}
