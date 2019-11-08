package com.reinkes.codingchallenge.codingchallenge.transformer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

public class SortingTransformer {

	private Optional<String> sorting;
	private String defaultSortingDirection;
	private List<String> validKeys;

	public SortingTransformer(Optional<String> sorting, String defaultSortingDirection, String[] validKeys) {
		this.sorting = sorting;
		this.defaultSortingDirection = defaultSortingDirection;
		this.validKeys = Arrays.asList(validKeys);
	}

	public Optional<LinkedList<SortVO>> transform() {
		if (this.sorting.isPresent()) {
			String[] allKeys = this.sorting.orElse("").split(",");
			LinkedList<SortVO> sortVOs = new LinkedList<>();
			for (String singleKeyWithDirection : allKeys) {
				String[] singleKeyAndDirection = singleKeyWithDirection.split(":");
				String key = singleKeyAndDirection[0];
				if(!validKeys.contains(key)) {
					continue;
				}
				String direction = this.defaultSortingDirection;
				if (singleKeyAndDirection.length > 1 && !StringUtils.isEmpty(singleKeyAndDirection[1])) {
					direction = singleKeyAndDirection[1];
				}
				sortVOs.add(new SortVO(key, direction));
			}
			return Optional.of(sortVOs);
		}
		return Optional.empty();
	}

}
