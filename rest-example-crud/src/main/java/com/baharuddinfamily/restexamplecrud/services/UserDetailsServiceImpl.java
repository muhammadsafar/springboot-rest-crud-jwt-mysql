package com.baharuddinfamily.restexamplecrud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.baharuddinfamily.restexamplecrud.model.User;
import com.baharuddinfamily.restexamplecrud.repos.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.getUserByUsername(username);

		if (user == null) {

			System.out.println("Could not find user");
			throw new UsernameNotFoundException("Could not find user");
		}

		return new MyUserDetails(user);
	}

}