package com.example.training3.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.example.training3.Models.User;
import com.example.training3.Repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@Component
public class DatabaseLoader {
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	private void initDatabase() {
		List<GrantedAuthority> adminRoles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"));
        List<GrantedAuthority> userRoles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        userRepository.save(new User("root", "root1", adminRoles));
        userRepository.save(new User("user", "user1", userRoles));
	}

}
