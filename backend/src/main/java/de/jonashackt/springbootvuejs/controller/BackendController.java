package de.jonashackt.springbootvuejs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import de.jonashackt.springbootvuejs.domain.User;
import de.jonashackt.springbootvuejs.repository.UserRepository;

@RestController()
@RequestMapping("/api")
public class BackendController {

	private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

	public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";

	@Autowired
	private UserRepository userRepository;

	@Value("${football-data.api-key}")
	private String footbalDataApiKey;

	@Value("${football-data.api-epl-matchday}")
	private String matchDayPLUrl;

	@RequestMapping(path = "/hello")
	public @ResponseBody String sayHello() {
		LOG.info("GET called on /hello resource");
		return HELLO_TEXT;
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody long addNewUser(@RequestParam String firstName, @RequestParam String lastName) {
		User user = new User(firstName, lastName);
		userRepository.save(user);

		LOG.info(user.toString() + " successfully saved into DB");

		return user.getId();
	}

	@GetMapping(path = "/user/{id}")
	public @ResponseBody User getUserById(@PathVariable("id") long id) {
		LOG.info("Reading user with id " + id + " from database.");
		return userRepository.findById(id).get();
	}

	@GetMapping(path = "/epl/{day}")
	public ResponseEntity<Match> getEPLByDay(@PathVariable("day") int day) {
		LOG.info("Reading match day details, day  " + day + " by calling api.");
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Auth-Token", footbalDataApiKey);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		try {
			ResponseEntity<Match> rest = restTemplate.exchange(matchDayPLUrl + day, HttpMethod.GET, entity,
					Match.class);
			return rest;
		} catch (RestClientException rce) {
			LOG.error(rce.getMessage(),rce);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
