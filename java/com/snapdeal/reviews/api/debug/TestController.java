package com.snapdeal.reviews.api.debug;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.snapdeal.reviews.api.rest.controller.BaseController;
import com.snapdeal.reviews.commons.debug.EmployeeBo;
import com.snapdeal.reviews.service.debug.TestService;

@RestController
public class TestController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private TestService testService;

	@RequestMapping(value = "/test/employee", method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody EmployeeBo employee) {
		String id = testService.create(employee);
		return new ResponseEntity<String>(id, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/test/employee/{id}")
	public ResponseEntity<EmployeeBo> read(@PathVariable(value = "id") String id) {
		EmployeeBo employee = testService.read(id);
		return new ResponseEntity<EmployeeBo>(employee, HttpStatus.OK);
	}

	@RequestMapping("/test")
	public ResponseEntity<Map<String, String>> test(UriComponentsBuilder uriBuilder) {
		LOGGER.debug("Creating Response");

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "Test");
		map.put("ok", "bye");

		HttpHeaders headers = new HttpHeaders();
		setLocationHeader(headers, buildURIComponent(uriBuilder, "/test", "1"));

		LOGGER.debug("Sending Response");

		return new ResponseEntity<Map<String, String>>(map, headers, HttpStatus.CREATED);
	}
}
