package com.javatechie.jwt.api.filter;

import com.javatechie.jwt.api.service.CustomUserDetailsService;
import com.javatechie.jwt.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil; //validate Token
	@Autowired
	private CustomUserDetailsService service; //Load data from H2 Database
	
	/**
	 * This is invoked for "/" and "/allUsers" invokation
	 * 1. Compare username(Token) vs  username(H2 Database)
	 * 2. Token is Expired (more than 10 hours )?
	 * 3. Spring Security related OBject poulated...
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		String token = null;
		String username = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			/* 1. Extract Token From the Header(i.e "Berare "+Token )
			 * 2. Extract Username from #1
			 */
			token = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			/* 3. check H2 database -->Find/Load User Model Object (username+password)
			 * 4. Populate UserDetail Object(Spring Security) with #3
			 * 5. Compare username(Token) vs  username(H2 Database)
			 * 5A. Token expired i.e 10 hours older or not !!
			 * 
			 * */
			UserDetails userDetails = service.loadUserByUsername(username);
			if (jwtUtil.validateToken(token, userDetails.getUsername())) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}
