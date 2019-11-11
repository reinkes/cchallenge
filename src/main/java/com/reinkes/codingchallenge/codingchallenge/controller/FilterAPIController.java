package com.reinkes.codingchallenge.codingchallenge.controller;

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
import com.reinkes.codingchallenge.codingchallenge.exception.NoResultException;
import com.reinkes.codingchallenge.codingchallenge.exception.UnexpectedParserException;
import com.reinkes.codingchallenge.codingchallenge.exception.UnknownSortingKeyException;
import com.reinkes.codingchallenge.codingchallenge.filter.FilterService;
import com.reinkes.codingchallenge.codingchallenge.filter.vo.FilteredLinkVO;
import com.reinkes.codingchallenge.codingchallenge.sort.SortService;
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

	@Autowired
	SortService sortingService;

	@Value("${default.sort.direction}")
	String defaultSortDirection;

	@Value("${valid.sort.keys}")
	String[] validSortKeys;

	@GetMapping("/links")
	public ResponseEntity<List<ResultVO>> findLinks(@RequestParam(required = false) String parent,
			@RequestParam(required = false) String sort)
			throws UnknownSortingKeyException, UnexpectedParserException, NoResultException {
		logger.info("Requesting findLinks with: parent=" + parent);
		Optional<String> tParent = new ParentTransformer().transform(Optional.ofNullable(parent));
		Optional<LinkedList<SortVO>> sortingKeys = new SortingTransformer(defaultSortDirection, validSortKeys)
				.transform(Optional.ofNullable(sort));

		Optional<ArrayList<FilteredLinkVO>> links = filterService.findLinks(tParent);
		if (sortingKeys.isPresent()) {
			links = sortingService.sortLinks(links, sortingKeys);
		}

		if (links.isPresent()) {
			return new ResponseEntity<>(links.get().stream().map(r -> map(r, tParent)).collect(Collectors.toList()),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ArrayList<ResultVO>(), HttpStatus.BAD_REQUEST);
		}
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
