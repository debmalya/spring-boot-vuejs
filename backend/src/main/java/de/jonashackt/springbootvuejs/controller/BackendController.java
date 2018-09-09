package de.jonashackt.springbootvuejs.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import de.jonashackt.springbootvuejs.domain.User;
import de.jonashackt.springbootvuejs.repository.UserRepository;
import jash.debmalya.springbootvuejs.domain.MatchDay;

@RestController()
@RequestMapping("/api")
public class BackendController {

	private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

	public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";

	private static HashMap<Integer, MatchDay> cache = new HashMap<>();

	private static HashMap<String, HashMap<Integer, MatchDay>> cacheCompetition = new HashMap<>();

	@Autowired
	private UserRepository userRepository;

	@Value("${football-data.api-key}")
	private String footbalDataApiKey;

	@Value("${football-data.api-epl-matchday}")
	private String matchDayPLUrl;

	@Value("${football-data.api-matchday}")
	private String matchDayUrl;

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

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(path = "/epl/{day}")
	public @ResponseBody MatchDay getEPLByDay(@PathVariable("day") int day) {
		LOG.info("Reading match day details, day  " + day + " by calling api.");
		MatchDay matchDay = cache.get(day);
		if (matchDay == null) {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Auth-Token", footbalDataApiKey);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			try {
				LOG.info("Called with headers :" + headers);
				ResponseEntity<MatchDay> rest = restTemplate.exchange(matchDayPLUrl + day, HttpMethod.GET, entity,
						MatchDay.class);
				matchDay = rest.getBody();
				cache.put(day, matchDay);

			} catch (RestClientException rce) {

				LOG.error(rce.getMessage(), rce);
				return null;
			}
		}
		return matchDay;

	}

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(path = "/comp/{competition}/{day}")
	public @ResponseBody MatchDay getByDay(@PathVariable("competition") String competition,
			@PathVariable("day") int day) {
		LOG.info("Reading match day comp '"+competition+"' details, day  '" + day + "' by calling api.");
		MatchDay matchDay = null;

		HashMap<Integer, MatchDay> competitionDayWise = cacheCompetition.get(competition);
		if (competitionDayWise != null) {
			matchDay = competitionDayWise.get(day);
		} else {
			competitionDayWise = new HashMap<>();
		}

		if (matchDay == null) {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Auth-Token", footbalDataApiKey);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			try {
				LOG.info("Called with headers :" + headers);
				matchDayUrl = matchDayUrl+competition+"/matches/?matchday=";
				LOG.info("matchDayUrl :" + matchDayUrl);
				ResponseEntity<MatchDay> rest = restTemplate.exchange(matchDayUrl + day, HttpMethod.GET, entity,
						MatchDay.class);
				matchDay = rest.getBody();
				competitionDayWise.put(day, matchDay);
				cacheCompetition.put(competition, competitionDayWise);

			} catch (RestClientException rce) {

				LOG.error(rce.getMessage(), rce);
				return null;
			}
		} else {
			LOG.info(competition + " match day " + day + " retrieved from cache");
		}
		return matchDay;

	}

}
