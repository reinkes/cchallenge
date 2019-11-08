package com.reinkes.codingchallenge.codingchallenge.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reinkes.codingchallenge.codingchallenge.domain.output.ResultVO;
import com.reinkes.codingchallenge.codingchallenge.service.FilterService;
import com.reinkes.codingchallenge.codingchallenge.service.vo.FilteredLinkVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.ParentTransformer;

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
	public ResponseEntity<List<ResultVO>> findLinks(@RequestParam(required = false) String parent) throws UnsupportedEncodingException {
		logger.info("Requesting findLinks with: parent=" + parent);
		ArrayList<FilteredLinkVO> response = filterService.findLinks(new ParentTransformer().transform(parent));
		logger.info("Responding findLinks");
		return new ResponseEntity<>(response.stream().map(r -> map(r)).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	private ResultVO map(FilteredLinkVO fLinkVO) {
		return new ResultVO(fLinkVO.joinPath(), fLinkVO.getUrl());
	}
		
}
