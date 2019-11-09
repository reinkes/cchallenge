package com.reinkes.codingchallenge.codingchallenge.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.reinkes.codingchallenge.codingchallenge.filter.vo.FilteredLinkVO;
import com.reinkes.codingchallenge.codingchallenge.transformer.SortVO;

@Component
public class SortService {

	@SuppressWarnings("unchecked")
	public ArrayList<FilteredLinkVO> sortLinks(ArrayList<FilteredLinkVO> links,
			Optional<LinkedList<SortVO>> sortingKeys) {

		if (sortingKeys.isPresent()) {
			Collections.sort(links, new FilteredLinkChain(sortingKeys.get().stream()
					.map(sk -> getComparatorBySortKey(sk)).collect(Collectors.toList()).toArray(new Comparator[0])));
		}

		return links;
	}

	private Comparator<FilteredLinkVO> getComparatorBySortKey(SortVO sortVO) {
		Comparator<FilteredLinkVO> c = null;
		switch (sortVO.getKey()) {
		case "label":
			c = new JoinPathComparator();
			break;
		case "url":
			c = new URLComparator();
			break;
		default:
			// TODO: better exception
			throw new RuntimeException("this should not happen!");
		}
		return "desc".equals(sortVO.getDirection()) ? c.reversed() : c;
	}

	
}
