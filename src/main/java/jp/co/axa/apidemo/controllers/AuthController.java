
package jp.co.axa.apidemo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.UserRepository;
import jp.co.axa.apidemo.security.JwtTokenProvider;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	UserRepository users;

	@Autowired
	CustomUserDetailsService userDetailsService;

	Logger logger = LogManager.getLogger(AuthController.class);

	/*
	 * Endpoint for signing in the user
	 */
	@PostMapping("/signin")
	public ResponseEntity signin(@RequestBody User data) {
		try {
			String username = data.getUsername();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
			String token = jwtTokenProvider.createToken(username, this.users.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			logger.info("Successfully signed In");
			return ResponseEntity.ok().body(model);
		} catch (AuthenticationException e) {
			logger.error("An exception occurred!", new BadCredentialsException("Invalid username/password supplied"));
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}

	/*
	 * Just for simulation this new user creating endpoint is created for testing
	 * the employee endpoint is protected or not
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}
}
