package com.reinkes.codingchallenge.codingchallenge.comparator;

import java.util.Comparator;

import com.reinkes.codingchallenge.codingchallenge.service.vo.FilteredLinkVO;

public class URLComparator implements Comparator<FilteredLinkVO>{

	@Override
	public int compare(FilteredLinkVO o1, FilteredLinkVO o2) {
		return o1.getUrl().compareTo(o2.getUrl());
	}

}
