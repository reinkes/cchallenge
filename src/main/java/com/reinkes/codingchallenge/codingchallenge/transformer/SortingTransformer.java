package com.reinkes.codingchallenge.codingchallenge.transformer;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.util.StringUtils;

public class SortingTransformer {

	private Optional<String> sorting;
	private String defaultSortingDirection;

	public SortingTransformer(Optional<String> sorting, String defaultSortingDirection) {
		this.sorting = sorting;
		this.defaultSortingDirection = defaultSortingDirection;
	}

	public Optional<LinkedList<SortVO>> transform() {
		if (this.sorting.isPresent()) {
			String[] sortKeys = this.sorting.orElse("").split(",");
			LinkedList<SortVO> sortVOs = new LinkedList<>();
			for (String key : sortKeys) {
				String[] keyAndDirection = key.split(":");
				String direction = this.defaultSortingDirection;
				if (keyAndDirection.length > 1 && !StringUtils.isEmpty(keyAndDirection[1])) {
					direction = keyAndDirection[1];
				}
				sortVOs.add(new SortVO(keyAndDirection[0], direction));
			}
			return Optional.of(sortVOs);
		}
		return Optional.empty();
	}

}
