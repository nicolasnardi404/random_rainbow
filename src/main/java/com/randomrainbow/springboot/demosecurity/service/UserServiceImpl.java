package com.randomrainbow.springboot.demosecurity.service;

import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.dao.RoleDao;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.util.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


	private RoleDao roleDao;

	private BCryptPasswordEncoder passwordEncoder;

	private UserRepository repository;

	@Autowired
	public UserServiceImpl( RoleDao roleDao, BCryptPasswordEncoder passwordEncoder,
			UserRepository repository) {
		this.roleDao = roleDao;
		this.passwordEncoder = passwordEncoder;
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = repository.findByUsername(userName)
               .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		User logIn = new User();
		logIn.setEmail(user.getEmail());
		logIn.setUsername(user.getUsername());
		logIn.setFirstName(user.getFirstName());
		logIn.setLastName(user.getLastName());
		logIn.setRole(user.getRole());

		return logIn;
	}	


	private String handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		if (e.getMessage().contains("user.username_UNIQUE")) {
			return "Username already exists";
		} else if (e.getMessage().contains("user.email_UNIQUE")) {
			return "Email already exists";
		} else {
			return "An unexpected error occurred";
		}	
	}

}
