package com.ssingh.covid19.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssingh.covid19.annotation.Loggable;
import com.ssingh.covid19.dto.UserDTO;
import com.ssingh.covid19.entity.UserBO;
import com.ssingh.covid19.exception.UserDetailsServiceException;
import com.ssingh.covid19.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDTO userDTO = fetchUserByUsername(username);
		return new User(userDTO.getUsername(), userDTO.getPassword(),
				new ArrayList<>());

	}
	
	public UserDTO fetchUserByUsername(String username){			
		Optional<UserBO> userBOOp = userRepository.findByUsername(username);
		if(!userBOOp.isPresent()){
			throw new UsernameNotFoundException("No Username found : "
					+ username);
		}
		UserBO userBO = userBOOp.get();		
		UserDTO userDto = new UserDTO();
		try {
			BeanUtils.copyProperties(userDto, userBO);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new UserDetailsServiceException(e);
		}
		return userDto;
	}

	public UserDTO addNewUser(UserDTO user) {
		UserBO userBO = new UserBO();
		userBO.setUsername(user.getUsername());
		System.out.println(passwordEncoder.encode(user.getPassword()));
		userBO.setPassword(passwordEncoder.encode(user.getPassword()));
		userBO.setName(user.getName());

		userBO = userRepository.save(userBO);

		user = new UserDTO(userBO.getUsername(), userBO.getName());
		return user;
	}
}
