package com.reinkes.codingchallenge.codingchallenge.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortingTransformer;

import ch.qos.logback.classic.Logger;

/**
 * 
 * @author reinkes
 *
 */

@RestController
@RequestMapping(path = "/")
public class FilterAPIController {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(FilterAPIController.class);

	@Autowired
	FilterService filterService;
	
	@Value("${default.sort.direction}")
	String defaultSortDirection;

	@GetMapping("/links")
	public ResponseEntity<List<ResultVO>> findLinks(@RequestParam(required = false) String parent,
			@RequestParam(required = false) String sort) throws UnsupportedEncodingException {
		logger.info("Requesting findLinks with: parent=" + parent);
		Optional<String> tParent = new ParentTransformer(Optional.ofNullable(parent)).transform();
		Optional<LinkedList<SortVO>> sortingKeys = new SortingTransformer(Optional.ofNullable(sort), defaultSortDirection).transform();
		
		ArrayList<FilteredLinkVO> response = filterService.findLinks(tParent);
		logger.info("Responding findLinks");
		return new ResponseEntity<>(response.stream().map(r -> map(r, tParent)).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	private ResultVO map(FilteredLinkVO fLinkVO, Optional<String> tParent) {
		fLinkVO.setPath(reduceListUntilParent(fLinkVO.getPath(), tParent));
		return new ResultVO(fLinkVO.joinPath(), fLinkVO.getUrl());
	}

	@SuppressWarnings("unchecked")
	private LinkedList<String> reduceListUntilParent(LinkedList<String> path, Optional<String> tParent) {
		if (tParent.isPresent()) {
			for (String elem : (LinkedList<String>) path.clone()) {
				if (!elem.equals(tParent.get())) {
					path.pop();
				} else {
					break;
				}
			}
		}
		return path;
	}

}
