package com.randomrainbow.springboot.demosecurity.service;

import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.dao.RoleDao;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

	// @Override
	// public Optional<User> findByUserName(String userName) {
	// 	// check the database if the user already exists
	// 	return userDao.findByUserName(userName);
	// }

	@Override
	public void save(WebUser webUser) {
		User user = new User();

		// assign user details to the user object
		user.setUsername(webUser.getUserName());
		user.setPassword(passwordEncoder.encode(webUser.getPassword()));
		user.setFirstName(webUser.getFirstName());
		user.setLastName(webUser.getLastName());
		user.setEmail(webUser.getEmail());
		user.setEmailVerified(true); // change to false later on ##TODO

		// give user default role of "employee"
		user.setRole(Role.ROLE_USER);

		// save user in the database
		repository.save(user);
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


}
