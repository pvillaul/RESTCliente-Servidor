package com.example.training3.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.training3.Models.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	@Autowired
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${rest.server.url}")
    private String restServerUrl;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		User user;
		try {
			user = restTemplate.getForObject(restServerUrl + "user/" + username,User.class);
		} catch(Exception e) {
			throw new BadCredentialsException("User not found");
		}
		//User user = userRepository.findById(username).orElseThrow(() -> new BadCredentialsException("User not found"));
		if (!new BCryptPasswordEncoder().matches(password, user.getContraseña())) {
			throw new BadCredentialsException("Wrong password");
		}

		return new UsernamePasswordAuthenticationToken(username, password, user.getRoles());

	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}
