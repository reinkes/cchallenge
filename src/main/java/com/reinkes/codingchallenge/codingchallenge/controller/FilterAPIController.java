package com.reinkes.codingchallenge.codingchallenge.controller;

import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reinkes.codingchallenge.codingchallenge.domain.result.ResultVO;
import com.reinkes.codingchallenge.codingchallenge.service.FilterService;

import ch.qos.logback.classic.Logger;

/**
 * 
 * @author reinkes
 *
 */

@RestController
@RequestMapping(path="/")
public class FilterAPIController {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(FilterAPIController.class);
	
	@Autowired
	FilterService filterService;
	
	@GetMapping("/links")
	public ResponseEntity<ArrayList<ResultVO>> findLinks() {
		logger.info("Requesting findLinks with: ");
		ArrayList<ResultVO> response = filterService.findLinks();
		logger.info("Responding findLinks");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
		
}
