/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baharuddinfamily.restexamplecrud.controller;

import com.baharuddinfamily.restexamplecrud.exception.DataNotFound;
import com.baharuddinfamily.restexamplecrud.jwt.JwtUtil;
import com.baharuddinfamily.restexamplecrud.model.AuthenticationResponse;
import com.baharuddinfamily.restexamplecrud.model.Tutorial;
import com.baharuddinfamily.restexamplecrud.model.User;
import com.baharuddinfamily.restexamplecrud.repos.TutorialRepository;
import com.baharuddinfamily.restexamplecrud.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User Muhammad Safar Baharuddin
 */
//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/tutorials")
public class TutorialController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUti;
	
	@Autowired
	private UserDetailsServiceImpl myUserDetailsServices;


	@Autowired
	private TutorialRepository tutorialRepository;

	@RequestMapping(path = "/sayhello")
	public String hello() {
		return "Hi, its works";
	}
	
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthentication(@RequestBody User user) throws Exception {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = myUserDetailsServices.loadUserByUsername(user.getUsername());
		final String jwt = jwtUti.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(user.getUsername(), user.getUsername(),
				userDetails.getAuthorities().toString(), jwt));
	}

	@GetMapping(path = "")
	public ResponseEntity<List<Tutorial>> lists(@RequestParam(required = false) String title) {

		try {
			List<Tutorial> datas = new ArrayList<Tutorial>();

			if (title == null) {
				tutorialRepository.findAll().forEach(datas::add);
			} else {
				tutorialRepository.findByTitleContaining(title).forEach(datas::add);
			}

			if (datas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(datas, HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") int id) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {

			throw new DataNotFound("Not found id-" + id);

		}
	}

	@PostMapping(path = "/save")
	public ResponseEntity<Tutorial> save(@RequestBody Tutorial tutorial) {

		try {

			Tutorial responseTutorial = new Tutorial();
			responseTutorial.setTitle(tutorial.getTitle());
			responseTutorial.setDescription(tutorial.getDescription());
			responseTutorial.setPublished(false);

			tutorialRepository.save(responseTutorial);

			return new ResponseEntity<>(responseTutorial, HttpStatus.CREATED);

		} catch (Exception e) {

			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") int id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") int id) {
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

}
